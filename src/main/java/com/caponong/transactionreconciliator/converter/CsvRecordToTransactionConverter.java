package com.caponong.transactionreconciliator.converter;

import com.caponong.transactionreconciliator.configuration.properties.CsvTransactionHeaderNames;
import com.caponong.transactionreconciliator.entity.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CsvRecordToTransactionConverter implements Converter<CSVRecord, Transaction> {

    @Autowired
    private CsvTransactionHeaderNames csvTransactionHeaderNames;

    @Override
    public Transaction convert(CSVRecord source) {
        return Transaction.builder()
                .profileName(getField(source, csvTransactionHeaderNames.getProfileName()))
                .transactionDate(getField(source, csvTransactionHeaderNames.getTransactionDate()))
                .transactionAmount(getField(source, csvTransactionHeaderNames.getTransactionAmount()))
                .transactionNarrative(getField(source, csvTransactionHeaderNames.getTransactionNarrative()))
                .transactionDescription(getField(source, csvTransactionHeaderNames.getTransactionDescription()))
                .transactionId(getField(source, csvTransactionHeaderNames.getTransactionId()))
                .transactionType(getField(source, csvTransactionHeaderNames.getTransactionType()))
                .walletReference(getField(source, csvTransactionHeaderNames.getWalletReference()))
                .build();
    }

    private String getField(CSVRecord source, String header) {
        String value = StringUtils.EMPTY;
        try {
            value = source.get(header);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
        return value;
    }
}
