package com.caponong.transactionreconciliator.services.calculation;

import com.caponong.transactionreconciliator.entity.Transaction;
import org.apache.commons.lang.StringUtils;

public class ProfileNameChecker implements TransactionFieldChecker{

    @Override
    public boolean isFieldMatch(Transaction transactionOne, Transaction transactionTwo) {
        return StringUtils.equalsIgnoreCase(transactionOne.getProfileName().trim(), transactionTwo.getProfileName().trim());
    }
}
