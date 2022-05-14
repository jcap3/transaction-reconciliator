package com.caponong.transactionreconciliator.controller;

import com.caponong.transactionreconciliator.model.MatchTransactionsCountResponse;
import com.caponong.transactionreconciliator.model.Response;
import com.caponong.transactionreconciliator.model.TransactionsUploadResponse;
import com.caponong.transactionreconciliator.model.UnmatchedTransactionsResponse;
import com.caponong.transactionreconciliator.services.ReconciliationService;
import com.caponong.transactionreconciliator.util.CsvFileUtil;
import com.caponong.transactionreconciliator.util.ResponseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/transactions")
public class TransactionsApiController implements TransactionsApi {

    @Autowired
    private ReconciliationService reconciliationService;

    @Override
    @PostMapping("/upload")
    public ResponseEntity<Response<TransactionsUploadResponse>> uploadTransaction(
            @RequestPart("firstTransactionSet") MultipartFile firstTransactionSet,
            @RequestPart("secondTransactionSet") MultipartFile secondTransactionSet) {

        CsvFileUtil.checkIfFileUploadedIsCsv(firstTransactionSet);
        CsvFileUtil.checkIfFileUploadedIsCsv(secondTransactionSet);

        return ResponseConverter.convert(reconciliationService.uploadTransaction(firstTransactionSet, secondTransactionSet));
    }

    @Override
    @GetMapping("/{reconciliationToken}/matchSummary")
    public ResponseEntity<Response<MatchTransactionsCountResponse>> matchedTransactionsCount(
            @PathVariable(name = "reconciliationToken") String reconciliationToken) {
        return ResponseConverter.convert(reconciliationService.getMatchSummary(reconciliationToken));
    }

    @Override
    @GetMapping("/{reconciliationToken}/unmatchedTransactionsSummary")
    public ResponseEntity<Response<UnmatchedTransactionsResponse>> unmatchedTransactions(
            @PathVariable(name = "reconciliationToken") String reconciliationToken) {
        return ResponseConverter.convert(reconciliationService.getUnmatchedTransactions(reconciliationToken));
    }
}
