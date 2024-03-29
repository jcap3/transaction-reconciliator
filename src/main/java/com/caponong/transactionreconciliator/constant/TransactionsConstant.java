package com.caponong.transactionreconciliator.constant;

public class TransactionsConstant {
    
    private TransactionsConstant() {}

    public static final String FIRST_TRANSACTION_IDENTIFIER = "a_";

    public static final String SECOND_TRANSACTION_IDENTIFIER = "b_";
    
    public static final String RECONCILIATION_TOKEN_PATTERN = "[\\w-]{36}";
    
    public static final String JAVA_TEMP_FOLDER = "java.io.tmpdir";
    
    public static final String RECONCILIATION_TOKEN_FOLDER = "/reconciliationTokens/";

}
