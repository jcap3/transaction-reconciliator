package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.entity.Transaction;
import com.caponong.transactionreconciliator.enums.ReconciliationRequestStatus;
import com.caponong.transactionreconciliator.error.exception.InternalServerError;
import com.caponong.transactionreconciliator.model.MultipartCsvFile;
import com.caponong.transactionreconciliator.services.ReconciliationRequestHandlerService;
import com.caponong.transactionreconciliator.services.TransactionsDbService;
import com.caponong.transactionreconciliator.services.Writer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DatabaseWriter implements Writer<MultipartCsvFile> {

    @Autowired
    private TransactionsDbService transactionsDbService;

    @Autowired
    private ReconciliationRequestHandlerService reconciliationRequestHandlerService;

    @Autowired
    @Qualifier("mvcConversionService")
    private ConversionService conversionService;
    
    @Value("#{${reconciliation-chunk-size}}")
    private int chunkSize;
    
    @Override
    @Async("threadPoolTaskExecutor")
    public void write(MultipartCsvFile data) {
        
        try (Reader reader = new BufferedReader(new InputStreamReader(data.getMultipartFile().getInputStream()))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .builder()
                    .setHeader().setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build());
            
            Iterator<CSVRecord> transactionsIterator = csvParser.iterator();
            List<Transaction> chunkedTransactions = new ArrayList<>();
            while(transactionsIterator.hasNext()) {
                Transaction transaction = conversionService.convert(transactionsIterator.next(), Transaction.class);
                Objects.requireNonNull(transaction).setReconciliationToken(data.getIdentifier());
                chunkedTransactions.add(transaction);
                if (chunkedTransactions.size() == chunkSize) {
                    transactionsDbService.save(chunkedTransactions);
                    chunkedTransactions.clear();
                }
            }
            
            if (!chunkedTransactions.isEmpty())
                transactionsDbService.save(chunkedTransactions);
            
        } catch (Exception e) {
            log.error("Service error", e);
            reconciliationRequestHandlerService.updateStatus(extractTokenFromIdentifier(data.getIdentifier()), ReconciliationRequestStatus.ERROR);
            throw new InternalServerError(e);
        }
        reconciliationRequestHandlerService.updateStatus(extractTokenFromIdentifier(data.getIdentifier()), ReconciliationRequestStatus.READY);
    }
    
    private String extractTokenFromIdentifier (String identifier) {
        return StringUtils.substring(identifier, 2);
    }

}
