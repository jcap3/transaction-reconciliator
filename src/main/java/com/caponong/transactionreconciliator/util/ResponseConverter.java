package com.caponong.transactionreconciliator.util;

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
}
