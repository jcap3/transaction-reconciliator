package com.caponong.transactionreconciliator.error.exception;

public class ParseDateError extends RuntimeException {
    public ParseDateError(Exception e) {
        super(e);
    }
}
