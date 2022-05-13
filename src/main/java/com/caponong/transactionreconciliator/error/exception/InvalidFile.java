package com.caponong.transactionreconciliator.error.exception;

public class InvalidFile extends RuntimeException {
    public InvalidFile (Exception e) {
        super(e);
    }
    
    public InvalidFile(String m) {
        super(m);
    }
}
