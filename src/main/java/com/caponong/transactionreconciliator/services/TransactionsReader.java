package com.caponong.transactionreconciliator.services;

import com.caponong.transactionreconciliator.model.MatchTransactionsCountResponse.FileMatchResult;

import java.util.concurrent.Future;

public interface TransactionsReader {

    Integer getTotalUniqueTransactions(String identifier, String token);
    
    Integer getMatchedTransactions (String reconciliationToken);

    Future<FileMatchResult> getFileMatchResult(String fileName, String 
            identifier, String reconciliationToken);
}
