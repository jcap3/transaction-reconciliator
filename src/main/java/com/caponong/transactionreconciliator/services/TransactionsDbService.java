package com.caponong.transactionreconciliator.services;

import com.caponong.transactionreconciliator.entity.Transaction;

public interface TransactionsDbService {

    void save(Transaction transaction);

    void deleteTransactionsByToken(String token);
    
    Integer getMatchCount (String identifierFile1, String identifierFile2, String token);
    
    Integer getTotalUniqueTransaction(String identifier, String token);
}
