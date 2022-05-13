package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.configuration.properties.CsvTransactionIndexFormatConfigProperties;
import com.caponong.transactionreconciliator.entity.Transaction;
import com.caponong.transactionreconciliator.enums.ReconciliationRequestStatus;
import com.caponong.transactionreconciliator.error.exception.DatabaseError;
import com.caponong.transactionreconciliator.error.exception.InternalServerError;
import com.caponong.transactionreconciliator.model.MultipartCsvFile;
import com.caponong.transactionreconciliator.services.ReconciliationRequestHandlerService;
import com.caponong.transactionreconciliator.services.TransactionsDbService;
import com.caponong.transactionreconciliator.services.Writer;
import com.caponong.transactionreconciliator.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Service
@Slf4j
public class DatabaseWriter implements Writer<MultipartCsvFile> {

    @Autowired
    private TransactionsDbService transactionsDbService;

    @Autowired
    private CsvTransactionIndexFormatConfigProperties fieldIndex;
    
    @Qualifier("reconciliationRequestHandlerServiceImpl")
    @Autowired
    private ReconciliationRequestHandlerService reconciliationRequestHandlerService;

    @Override
    @Async("threadPoolTaskExecutor")
    public void write(MultipartCsvFile data) {

        try (InputStream inputStream = data.getMultipartFile().getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            for (int a = 0; (line = bufferedReader.readLine()) != null; a++) {
                if (a == 0) {
                    continue;
                }
                transactionsDbService.save(buildTransaction(line, data));
                log.debug(String.valueOf(a));
            }
        } catch (IOException e) {
            log.error("Service error", e);
            reconciliationRequestHandlerService.updateStatus(extractTokenFromIdentifier(data.getIdentifier()), ReconciliationRequestStatus.ERROR);
            throw new InternalServerError(e);
        }
        reconciliationRequestHandlerService.updateStatus(extractTokenFromIdentifier(data.getIdentifier()), ReconciliationRequestStatus.READY);
    }

    private Transaction buildTransaction(String oneLinedFields, MultipartCsvFile data) {
        String[] separatedFields = oneLinedFields.split(",");
        return Transaction.builder()
                .profileName(getField(separatedFields, fieldIndex.getProfileName()))
                .transactionDate(DateUtil.parseDateString(getField(separatedFields, fieldIndex.getTransactionDate())))
                .transactionAmount(new BigDecimal(getField(separatedFields, fieldIndex.getTransactionAmount())))
                .transactionNarrative(getField(separatedFields, fieldIndex.getTransactionNarrative()))
                .transactionDescription(getField(separatedFields, fieldIndex.getTransactionDescription()))
                .transactionId(getField(separatedFields, fieldIndex.getTransactionId()))
                .transactionType(getField(separatedFields, fieldIndex.getTransactionType()))
                .walletReference(getField(separatedFields, fieldIndex.getWalletReference()))
                .reconciliationToken(data.getIdentifier())
                .build();
    }

    private String getField(String[] separatedFields, int index) {
        return index > separatedFields.length - 1 ? StringUtils.EMPTY : separatedFields[index];
    }
    
    private String extractTokenFromIdentifier (String identifier) {
        return StringUtils.substring(identifier, 2);
    }

}
