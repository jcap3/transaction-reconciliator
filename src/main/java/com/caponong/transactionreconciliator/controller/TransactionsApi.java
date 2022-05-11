package com.caponong.transactionreconciliator.controller;

import com.caponong.transactionreconciliator.model.MatchTransactionsCountResponse;
import com.caponong.transactionreconciliator.model.Response;
import com.caponong.transactionreconciliator.model.TransactionsUploadResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = {"Transactions"}, description = "Transactions Reconciliation APIs")
public interface TransactionsApi {

    @ApiOperation(value = "uploadTransaction",
            notes = "Invoking this endpoint allows client to upload transactions in csv format to be checked for matches",
            response = TransactionsUploadResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = TransactionsUploadResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Response.class)})
    ResponseEntity<Response<TransactionsUploadResponse>> uploadTransaction(
            @RequestPart("firstTransactionSet") MultipartFile firstTransactionSet,
            @RequestPart("secondTransactionSet") MultipartFile secondTransactionSet);

    @ApiOperation(value = "matchedTransactions",
            notes = "Invoking this endpoint allows client to get the transactions match count from the csv files uploaded",
            response = MatchTransactionsCountResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = MatchTransactionsCountResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Response.class)})
    ResponseEntity<Response<MatchTransactionsCountResponse>> matchedTransactionsCount(
            @PathVariable(name = "reconciliationToken") String reconciliationToken);
}
