package com.caponong.transactionreconciliator.error;

public class TransactionReconciliatorErrorCodes {
    
    private TransactionReconciliatorErrorCodes() {}

    public static final ErrorCode INTERNAL_ERROR = createErrorCode("999", "Internal Server Error");
    
    public static final ErrorCode MISSING_CSV_FILE_REQUEST = createErrorCode("001", "Missing request part");
    
    public static final ErrorCode INVALID_FILE_UPLOADED = createErrorCode("002", "File must be in .csv");
    
    public static final ErrorCode INVALID_RECONCILIATION_TOKEN = createErrorCode("003", "token should only contain numbers, letters, and - with length of 36");
    
    public static final ErrorCode REQUEST_NOT_YET_READY = createErrorCode("004", "Requested token not yet ready");
    
    public static final ErrorCode REQUEST_ERROR_OUT = createErrorCode("005", "Requested token encountered an error");
    
    public static final ErrorCode REQUEST_NOT_FOUND = createErrorCode("006", "Request not found");
    
    private static ErrorCode createErrorCode(String code, String message) {
        return new ErrorCode(code, message);
    }
    
            
}
