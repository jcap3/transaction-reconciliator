package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.configuration.properties.CsvTransactionIndexFormatConfigProperties;
import com.caponong.transactionreconciliator.entity.Transaction;
import com.caponong.transactionreconciliator.model.MultipartCsvFile;
import com.caponong.transactionreconciliator.repository.TransactionRepository;
import com.caponong.transactionreconciliator.services.Writer;
import com.caponong.transactionreconciliator.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Service
@Slf4j
public class DatabaseWriter implements Writer<MultipartCsvFile> {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private CsvTransactionIndexFormatConfigProperties fieldIndex;

    @Transactional
    @Override
    public void write(MultipartCsvFile data) {

        try (InputStream inputStream = data.getMultipartFile().getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            for (int a = 0; (line = bufferedReader.readLine()) != null; a++) {
                if (a == 0) {
                    continue;
                }
                repository.save(buildTransaction(line, data));
                log.info(String.valueOf(a));
            }
        } catch (IOException e) {
            log.error("Service error", e);
        }
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
        return index > separatedFields.length - 1? StringUtils.EMPTY : separatedFields[index];
    }

}
