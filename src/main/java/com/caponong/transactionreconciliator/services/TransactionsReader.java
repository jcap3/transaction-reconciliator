package com.caponong.transactionreconciliator.services;

import com.caponong.transactionreconciliator.entity.Transaction;
import com.caponong.transactionreconciliator.model.MatchTransactionsCountResponse.FileMatchResult;
import com.caponong.transactionreconciliator.model.UnmatchedTransaction;

import java.util.List;
import java.util.concurrent.Future;

public interface TransactionsReader {

    int getTotalUniqueTransactions(String identifier, String token);

    int getTotalUniqueTransactions(String token);

    int getMatchedTransactions(String reconciliationToken);

    List<Transaction> getUnmatchedTransactions(String identifier, String reconciliationToken);

    List<Transaction> getPotentialMatches(Transaction transaction);

    Future<List<UnmatchedTransaction>> getUnmatchedTransactionSummary(String identifier,
                                                                      String reconciliationToken);

    Future<FileMatchResult> getFileMatchResult(String fileName, String
            identifier, String reconciliationToken);
}
