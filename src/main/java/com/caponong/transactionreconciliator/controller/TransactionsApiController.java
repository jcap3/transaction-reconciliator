package com.caponong.transactionreconciliator.controller;

import com.caponong.transactionreconciliator.model.Response;
import com.caponong.transactionreconciliator.model.TransactionsUploadResponse;
import com.caponong.transactionreconciliator.services.ReconciliationService;
import com.caponong.transactionreconciliator.util.ResponseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/transaction")
public class TransactionsApiController implements TransactionsApi {

    @Autowired
    private ReconciliationService reconciliationService;    
    
    @Override
    @PostMapping()
    public ResponseEntity<Response<TransactionsUploadResponse>> uploadTransaction(
            @RequestPart("firstTransactionSet") MultipartFile firstTransactionSet,
            @RequestPart("secondTransactionSet") MultipartFile secondTransactionSet) {

        return ResponseConverter.convert(reconciliationService.uploadTransaction(firstTransactionSet,secondTransactionSet));
    }
    
    @GetMapping("/temp")
    public String tempGet() {
        return "Hello";
    }
}
