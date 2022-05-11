package com.caponong.transactionreconciliator.error.exception;

public class DatabaseError extends RuntimeException{
    public DatabaseError(Exception e) {
        super(e);
    }
}
