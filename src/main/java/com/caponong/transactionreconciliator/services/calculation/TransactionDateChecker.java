package com.caponong.transactionreconciliator.services.calculation;

import com.caponong.transactionreconciliator.entity.Transaction;

public class TransactionDateChecker implements TransactionFieldChecker{

    @Override
    public boolean isFieldMatch(Transaction transactionOne, Transaction transactionTwo) {
        return transactionOne.getTransactionDate().equals(transactionTwo.getTransactionDate());
    }
}
