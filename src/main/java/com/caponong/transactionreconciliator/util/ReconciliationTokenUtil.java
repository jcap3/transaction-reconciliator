package com.caponong.transactionreconciliator.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import static com.caponong.transactionreconciliator.constant.TransactionsConstant.FIRST_TRANSACTION_IDENTIFIER;
import static com.caponong.transactionreconciliator.constant.TransactionsConstant.SECOND_TRANSACTION_IDENTIFIER;

@Slf4j
public class ReconciliationTokenUtil {

    private ReconciliationTokenUtil() {}
    
    public static String toOppositeFileIdentifier(String reconciliationToken) {
        if (StringUtils.contains(reconciliationToken, FIRST_TRANSACTION_IDENTIFIER))
            return reconciliationToken.replaceFirst("^" + FIRST_TRANSACTION_IDENTIFIER, SECOND_TRANSACTION_IDENTIFIER);
        else
            return reconciliationToken.replaceFirst("^" + SECOND_TRANSACTION_IDENTIFIER, FIRST_TRANSACTION_IDENTIFIER);
    }
}
