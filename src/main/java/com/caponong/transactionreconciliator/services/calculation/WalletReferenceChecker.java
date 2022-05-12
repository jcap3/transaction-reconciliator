package com.caponong.transactionreconciliator.services.calculation;

import com.caponong.transactionreconciliator.entity.Transaction;
import org.apache.commons.lang.StringUtils;

public class WalletReferenceChecker implements TransactionFieldChecker{

    @Override
    public boolean isFieldMatch(Transaction transactionOne, Transaction transactionTwo) {
        return StringUtils.equalsIgnoreCase(transactionOne.getWalletReference().trim(),
                transactionTwo.getWalletReference().trim());
    }
}
