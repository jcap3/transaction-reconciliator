package com.caponong.transactionreconciliator.controller;

import com.caponong.transactionreconciliator.model.Response;
import com.caponong.transactionreconciliator.model.TransactionsUploadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/transaction")
public class TransactionsApiController implements TransactionsApi {

    @Override
    @PostMapping()
    public ResponseEntity<Response<TransactionsUploadResponse>> uploadTransaction(MultipartFile[] transactions) {
        return null;
    }
    
    @GetMapping("/temp")
    public String tempGet() {
        return "Hello";
    }
}
