package com.caponong.transactionreconciliator.error.exception;

public class RequestTokenNotFound extends RuntimeException {

    public RequestTokenNotFound(Exception e) {
        super(e);
    }

    public RequestTokenNotFound(String s) {
        super(s);
    }

}
