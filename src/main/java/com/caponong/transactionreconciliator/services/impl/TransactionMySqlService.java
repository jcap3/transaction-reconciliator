package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.entity.Transaction;
import com.caponong.transactionreconciliator.repository.TransactionRepository;
import com.caponong.transactionreconciliator.services.TransactionsDbService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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

    @Override
    public Integer getMatchCount(String identifierFile1, String identifierFile2, String token) {
        String likeExpressionFile1 = StringUtils.join(new String[]{identifierFile1, token, "%"});
        String likeExpressionFile2 = StringUtils.join(new String[]{identifierFile2, token, "%"});
        return repository.getMatchedTransactionsCount(likeExpressionFile1, likeExpressionFile2);
    }

    @Override
    public Integer getTotalUniqueTransaction(String identifier, String token) {
        String likeExpression = StringUtils.join(new String[] {identifier, token, "%"});
        return repository.getTotalUniqueTransactions(likeExpression);
    }
}
