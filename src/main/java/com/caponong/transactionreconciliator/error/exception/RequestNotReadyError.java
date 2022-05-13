package com.caponong.transactionreconciliator.error.exception;

public class RequestNotReadyError extends RuntimeException{
    public RequestNotReadyError(Exception e){
        super(e);
    }

    public RequestNotReadyError(String m){
        super(m);
    }

}
