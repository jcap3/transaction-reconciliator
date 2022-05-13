package com.caponong.transactionreconciliator.transaction.matchsummary;

import com.caponong.transactionreconciliator.ApplicationTest;
import com.caponong.transactionreconciliator.model.Response;
import com.caponong.transactionreconciliator.model.TransactionsUploadResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TransactionMatchSuccessIT extends ApplicationTest {

    @Test
    public void testSuccessMatch() throws Exception {

        String reconciliationToken = executeUploadAndGetReconciliationToken("testDataCsvOneMatch_FirstFile.csv",
                "testDataCsvOneMatch_SecondFile.csv");

        mockMvc.perform(
                get(BASE_PATH + reconciliationToken + TRANSACTION_MATCH_SUMMARY_API)
                        .servletPath(BASE_PATH + reconciliationToken + TRANSACTION_MATCH_SUMMARY_API))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("0")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.body.firstTransactionSet.fileName", is("testDataCsvOneMatch_FirstFile.csv")))
                .andExpect(jsonPath("$.body.firstTransactionSet.totalUniqueRecords", is(4)))
                .andExpect(jsonPath("$.body.firstTransactionSet.matchedTransactions", is(1)))
                .andExpect(jsonPath("$.body.firstTransactionSet.unmatchedTransactions", is(3)))
                .andExpect(jsonPath("$.body.secondTransactionSet.fileName", is("testDataCsvOneMatch_SecondFile.csv")))
                .andExpect(jsonPath("$.body.secondTransactionSet.totalUniqueRecords", is(4)))
                .andExpect(jsonPath("$.body.secondTransactionSet.matchedTransactions", is(1)))
                .andExpect(jsonPath("$.body.secondTransactionSet.unmatchedTransactions", is(3)))
                .andDo(print());
    }
    
    @Test // todo
    public void testSuccessMatchChunkedData() throws Exception {
        String reconciliationToken = executeUploadAndGetReconciliationToken("testDataCsvOneMatch_FirstFile.csv",
                "testDataCsvOneMatch_SecondFile.csv");
    }
    
    private String executeUploadAndGetReconciliationToken(String fileName1, String fileName2) throws Exception {
        String response = mockMvc.perform(
                multipart(TRANSACTION_UPLOAD_API)
                        .file(getTestDataCsv("firstTransactionSet", fileName1))
                        .file(getTestDataCsv("secondTransactionSet", fileName2))
                        .servletPath(TRANSACTION_UPLOAD_API)

        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        wait(500);
        
        JsonNode rootNode = objectMapper.readTree(response);
        return rootNode.get("body").get("reconciliationToken").textValue();
    }
}
