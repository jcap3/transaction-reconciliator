package com.caponong.transactionreconciliator.services;

public interface TransactionsReader {

    Integer getTotalUniqueTransactions(String identifier, String token);
    
    Integer getMatchedTransactions (String reconciliationToken);
}
