package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.entity.Transaction;
import com.caponong.transactionreconciliator.repository.TransactionRepository;
import com.caponong.transactionreconciliator.services.TransactionsDbService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.caponong.transactionreconciliator.constant.TransactionsConstant.FIRST_TRANSACTION_IDENTIFIER;
import static com.caponong.transactionreconciliator.constant.TransactionsConstant.SECOND_TRANSACTION_IDENTIFIER;

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
    public int getMatchCount(String token) {
        String likeExpressionFile1 = createLikeExpression(token, FIRST_TRANSACTION_IDENTIFIER);
        String likeExpressionFile2 = createLikeExpression(token, SECOND_TRANSACTION_IDENTIFIER);
        return repository.getMatchedTransactionsCount(likeExpressionFile1, likeExpressionFile2);
    }

    @Override
    public int getTotalUniqueTransaction(String identifier, String token) {
        String likeExpression = createLikeExpression(token, identifier);
        return repository.getTotalUniqueTransactions(likeExpression);
    }

    @Override
    public int getTotalUniqueTransaction(String token) {
        return repository.getTotalUniqueTransactions(token);
    }

    @Override
    public List<Transaction> getUnMatchedTransactions(String identifier, String token) {
        String likeExpressionFile1 = createLikeExpression(token, FIRST_TRANSACTION_IDENTIFIER);
        String likeExpressionFile2 = createLikeExpression(token, SECOND_TRANSACTION_IDENTIFIER);
        
        if (StringUtils.equalsIgnoreCase(identifier, FIRST_TRANSACTION_IDENTIFIER))
            return repository.getFirstFileUnMatchedTransactions(likeExpressionFile1, likeExpressionFile2);
        else
            return repository.getSecondFileUnMatchedTransactions(likeExpressionFile1, likeExpressionFile2);
    }

    @Override
    public List<Transaction> getTransactionsByToken(String token, Pageable page) {
        return repository.findByReconciliationTokenContaining(token, page);
    }

    private String createLikeExpression(String token, String identifier) {
        return StringUtils.join(new String[]{identifier, token, "%"});
    }

}
