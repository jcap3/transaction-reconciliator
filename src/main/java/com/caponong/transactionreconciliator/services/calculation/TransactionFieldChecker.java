package com.caponong.transactionreconciliator.services.calculation;

import com.caponong.transactionreconciliator.entity.Transaction;

public interface TransactionFieldChecker {
    
    boolean isFieldMatch(Transaction transactionOne, Transaction transactionTwo);
    
}
