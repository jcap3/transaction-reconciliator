package com.caponong.transactionreconciliator.error.exception;

public class InternalServerError extends RuntimeException{
    public InternalServerError(Exception e) {
        super(e);
    }
}
