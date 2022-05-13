package com.caponong.transactionreconciliator.util;

import com.caponong.transactionreconciliator.error.ErrorCode;
import com.caponong.transactionreconciliator.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseConverter {
    
    private ResponseConverter() {}
    
    public static <T> ResponseEntity<Response<T>> convert(T serviceResponse) {
        Response<T> response = new Response<>();
        response.setBody(serviceResponse);
        response.setMessage("success");
        response.setCode("0");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    public static Response<Void> convertError(ErrorCode errorCode) {
        Response<Void> response = new Response<>();
        response.setMessage("error");
        response.setCode("999");
        response.setErrorCode(errorCode);
        
        return response;
    }
}
