package com.caponong.transactionreconciliator.error.exception;

public class RequestInterruptedError extends RuntimeException{
    public RequestInterruptedError(Exception e) {
        super(e);
    }

    public RequestInterruptedError(String m) {
        super(m);
    }

}
