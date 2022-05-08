package com.caponong.transactionreconciliator.controller;

import com.caponong.transactionreconciliator.model.Response;
import com.caponong.transactionreconciliator.model.TransactionsUploadResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Api
public interface TransactionsApi {

    @ApiOperation(value = "", notes = "Invoking this endpoint allows ", response = TransactionsUploadResponse.class, tags = {"upload", "csv", "transactions"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = TransactionsUploadResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Response.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Response.class)})
    ResponseEntity<Response<TransactionsUploadResponse>> uploadTransaction (
            
            
    @RequestParam("transactions") MultipartFile[] transactions);
}
