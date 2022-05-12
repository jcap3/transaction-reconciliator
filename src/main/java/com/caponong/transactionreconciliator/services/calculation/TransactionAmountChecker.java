package com.caponong.transactionreconciliator.services.calculation;

import com.caponong.transactionreconciliator.entity.Transaction;

public class TransactionAmountChecker implements TransactionFieldChecker{

    @Override
    public boolean isFieldMatch(Transaction transactionOne, Transaction transactionTwo) {
        return transactionOne.getTransactionAmount().equals(transactionTwo.getTransactionAmount());
    }
}
