package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.entity.Transaction;
import com.caponong.transactionreconciliator.repository.TransactionRepository;
import com.caponong.transactionreconciliator.services.TransactionsDbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class TransactionMySqlService implements TransactionsDbService {

    @Autowired
    private TransactionRepository repository;
    
    @Transactional
    @Override
    public void save(Transaction transaction) {
        repository.save(transaction);
    }

    @Transactional
    @Override
    public void deleteTransactionsByToken(String token) {
        repository.deleteByReconciliationTokenContaining(token);
    }
}
