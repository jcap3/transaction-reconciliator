package com.caponong.transactionreconciliator.services;

import com.caponong.transactionreconciliator.entity.Transaction;

public interface TransactionsDbService {

    void save(Transaction transaction);

    void deleteTransactionsByToken(String token);
}
