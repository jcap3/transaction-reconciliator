package com.caponong.transactionreconciliator.services;

import com.caponong.transactionreconciliator.entity.Transaction;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionsDbService {

    void save(Transaction transaction);

    void deleteTransactionsByToken(String token);
    
    int getMatchCount (String token);
    
    int getTotalUniqueTransaction(String identifier, String token);
    
    int getTotalUniqueTransaction(String token);
    
    List<Transaction> getUnMatchedTransactions(String identifier, String token);
    
    List<Transaction> getTransactionsByToken(String token, Pageable page);
}
