package com.caponong.transactionreconciliator.services.calculation;

import com.caponong.transactionreconciliator.entity.Transaction;
import org.apache.commons.lang.StringUtils;

public class TransactionIdChecker implements TransactionFieldChecker{
    
    @Override
    public boolean isFieldMatch(Transaction transactionOne, Transaction transactionTwo) {
        return StringUtils.equalsIgnoreCase(transactionOne.getTransactionId().trim(), transactionTwo.getTransactionId().trim());
    }
}
