package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.model.MatchTransactionsCountResponse.FileMatchResult;
import com.caponong.transactionreconciliator.services.TransactionsDbService;
import com.caponong.transactionreconciliator.services.TransactionsReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import static com.caponong.transactionreconciliator.constant.TransactionsConstant.FIRST_TRANSACTION_IDENTIFIER;
import static com.caponong.transactionreconciliator.constant.TransactionsConstant.SECOND_TRANSACTION_IDENTIFIER;

import java.util.concurrent.Future;

@Service
public class TransactionReconciliator implements TransactionsReader {

    @Autowired
    private TransactionsDbService transactionsDbService;

    @Override
    public Integer getTotalUniqueTransactions(String identifier, String token) {
        return transactionsDbService.getTotalUniqueTransaction(identifier, token);
    }

    @Override
    public Integer getMatchedTransactions(String reconciliationToken) {
        return transactionsDbService.getMatchCount(FIRST_TRANSACTION_IDENTIFIER, SECOND_TRANSACTION_IDENTIFIER, reconciliationToken);
    }

    @Async("threadPoolTaskExecutor")
    public Future<FileMatchResult> getFileMatchResult(String fileName, String identifier, String reconciliationToken) {
        Integer matchedTransactions = getMatchedTransactions(reconciliationToken);
        Integer totalUniqueRecords = getTotalUniqueTransactions(identifier, reconciliationToken);
        return new AsyncResult<>(
                FileMatchResult.builder()
                .fileName(fileName)
                .matchedTransactions(matchedTransactions)
                .totalUniqueRecords(totalUniqueRecords)
                .unmatchedTransactions(Math.max(totalUniqueRecords - matchedTransactions, 0))
                .build()
        );
    }
}
