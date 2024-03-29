package com.caponong.transactionreconciliator.model;

import com.caponong.transactionreconciliator.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private String message;

    private String code;

    private T body;
    
    private ErrorCode errorCode;
}