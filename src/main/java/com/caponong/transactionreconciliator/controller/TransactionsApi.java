package com.caponong.transactionreconciliator.controller;

import com.caponong.transactionreconciliator.model.MatchTransactionsCountResponse;
import com.caponong.transactionreconciliator.model.Response;
import com.caponong.transactionreconciliator.model.TransactionsUploadResponse;
import com.caponong.transactionreconciliator.model.UnmatchedTransactionsResponse;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;

import static com.caponong.transactionreconciliator.constant.TransactionsConstant.RECONCILIATION_TOKEN_PATTERN;

@SuppressWarnings("deprecation")
@Api(tags = {"Transactions"}, description = "Transactions Reconciliation APIs")
public interface TransactionsApi {

    @ApiOperation(value = "uploadTransaction",
            notes = "Invoking this endpoint allows client to upload transactions in csv format to be checked for matches",
            response = TransactionsUploadResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = TransactionsUploadResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class)})
    ResponseEntity<Response<TransactionsUploadResponse>> uploadTransaction(
            @RequestPart("firstTransactionSet") @ApiParam(value = "First TransactionSet to compare", required = true) MultipartFile firstTransactionSet,
            @RequestPart("secondTransactionSet") @ApiParam(value = "Second TransactionSet to compare", required = true) MultipartFile secondTransactionSet);

    @ApiOperation(value = "matchedTransactions",
            notes = "Invoking this endpoint allows client to get the transactions match count from the csv files uploaded",
            response = MatchTransactionsCountResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = MatchTransactionsCountResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class),
            @ApiResponse(code = 503, message = "Unavailable", response = Response.class)})
    ResponseEntity<Response<MatchTransactionsCountResponse>> matchedTransactionsCount(
            @Pattern(regexp = RECONCILIATION_TOKEN_PATTERN)
            @PathVariable(name = "reconciliationToken") 
            @ApiParam(value = "token used to process uploaded csv files", required = true) String reconciliationToken);

    @ApiOperation(value = "unmatchedTransactions",
            notes = "Invoking this endpoint allows client to get the unmatched transactions and its possible matches",
            response = UnmatchedTransactionsResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = UnmatchedTransactionsResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class),
            @ApiResponse(code = 503, message = "Unavailable", response = Response.class)})
    ResponseEntity<Response<UnmatchedTransactionsResponse>> unmatchedTransactions(
            @Pattern(regexp = RECONCILIATION_TOKEN_PATTERN)
            @PathVariable(name = "reconciliationToken") 
            @ApiParam(value = "token used to process uploaded csv files", required = true) String reconciliationToken);
}
