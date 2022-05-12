package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.entity.Transaction;
import com.caponong.transactionreconciliator.model.MatchTransactionsCountResponse.FileMatchResult;
import com.caponong.transactionreconciliator.model.TransactionModel;
import com.caponong.transactionreconciliator.model.UnmatchedTransaction;
import com.caponong.transactionreconciliator.services.TransactionsDbService;
import com.caponong.transactionreconciliator.services.TransactionsReader;
import com.caponong.transactionreconciliator.services.calculation.TransactionMatchCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.caponong.transactionreconciliator.util.ReconciliationTokenUtil.toOppositeFileIdentifier;

@Service
public class TransactionReconciliator implements TransactionsReader {

    @Autowired
    private TransactionsDbService transactionsDbService;

    @Autowired
    private TransactionMatchCalculator transactionMatchCalculator;

    @Autowired
    @Qualifier("mvcConversionService")
    private ConversionService conversionService;

    @Value("#{${reconciliation-chunk-size}}")
    private int chunkSize;

    @Override
    public int getTotalUniqueTransactions(String identifier, String token) {
        return transactionsDbService.getTotalUniqueTransaction(identifier, token);
    }

    @Override
    public int getTotalUniqueTransactions(String token) {
        return transactionsDbService.getTotalUniqueTransaction(token);
    }

    @Override
    public int getMatchedTransactions(String reconciliationToken) {
        return transactionsDbService.getMatchCount(reconciliationToken);
    }

    @Override
    public List<Transaction> getUnmatchedTransactions(String identifier, String reconciliationToken) {
        return transactionsDbService.getUnMatchedTransactions(identifier, reconciliationToken);
    }

    @Override
    public List<Transaction> getPotentialMatches(Transaction transaction) {
        List<Transaction> potentialMatches = new ArrayList<>();
        int totalPotentialMatchesPool = getTotalUniqueTransactions(toOppositeFileIdentifier(transaction.getReconciliationToken()));
        int chunkedRetrievalCount = (totalPotentialMatchesPool / chunkSize) + (totalPotentialMatchesPool % chunkSize == 0 ? 0 : 1);

        for (int a = 0; a < chunkedRetrievalCount; a++) {
            List<Transaction> potentialMatchesPool =
                    transactionsDbService.getTransactionsByToken(toOppositeFileIdentifier(transaction.getReconciliationToken()),
                            PageRequest.of(a, chunkSize));
            potentialMatchesPool.forEach(potentialMatch -> {
                if (transactionMatchCalculator.isPossibleMatch(transaction, potentialMatch))
                    potentialMatches.add(potentialMatch);
            });
        }

        return potentialMatches;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public Future<List<UnmatchedTransaction>> getUnmatchedTransactionSummary(String identifier,
                                                                             String reconciliationToken) {
        List<UnmatchedTransaction> unmatchedTransactionsSummary = new ArrayList<>();

        List<Transaction> unmatchedTransactions = getUnmatchedTransactions(identifier, reconciliationToken);
        unmatchedTransactions.forEach(transaction -> {
            UnmatchedTransaction unmatchedTransaction = UnmatchedTransaction.builder()
                    .transaction(conversionService.convert(transaction, TransactionModel.class))
                    .potentialMatches(getPotentialMatches(transaction).stream().map(a ->
                            conversionService.convert(a, TransactionModel.class)).collect(Collectors.toList()))
                    .build();
            unmatchedTransactionsSummary.add(unmatchedTransaction);
        });

        return new AsyncResult<>(unmatchedTransactionsSummary);
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public Future<FileMatchResult> getFileMatchResult(String fileName, String identifier, String reconciliationToken) {
        int matchedTransactions = getMatchedTransactions(reconciliationToken);
        int totalUniqueRecords = getTotalUniqueTransactions(identifier, reconciliationToken);
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
