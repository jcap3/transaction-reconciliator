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

        String reconciliationToken = executeUploadAndGetReconciliationToken("testDataCsvOneMatch_FirstFile.csv", 4,
                "testDataCsvOneMatch_SecondFile.csv", 4);

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
    
    @Test
    public void testSuccessMatchChunkedData() throws Exception {
        String reconciliationToken = executeUploadAndGetReconciliationToken("testDataCsvUnequalTotalUniqueRowsChunked_FirstFile.csv", 17,
                "testDataCsvUnequalTotalUniqueRowsChunked_SecondFile.csv", 15);

        mockMvc.perform(
                get(BASE_PATH + reconciliationToken + TRANSACTION_MATCH_SUMMARY_API)
                        .servletPath(BASE_PATH + reconciliationToken + TRANSACTION_MATCH_SUMMARY_API))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("0")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.body.firstTransactionSet.fileName", is("testDataCsvUnequalTotalUniqueRowsChunked_FirstFile.csv")))
                .andExpect(jsonPath("$.body.firstTransactionSet.totalUniqueRecords", is(17)))
                .andExpect(jsonPath("$.body.firstTransactionSet.matchedTransactions", is(13)))
                .andExpect(jsonPath("$.body.firstTransactionSet.unmatchedTransactions", is(4)))
                .andExpect(jsonPath("$.body.secondTransactionSet.fileName", is("testDataCsvUnequalTotalUniqueRowsChunked_SecondFile.csv")))
                .andExpect(jsonPath("$.body.secondTransactionSet.totalUniqueRecords", is(15)))
                .andExpect(jsonPath("$.body.secondTransactionSet.matchedTransactions", is(13)))
                .andExpect(jsonPath("$.body.secondTransactionSet.unmatchedTransactions", is(2)))
                .andDo(print());
    }
    
    @Test
    public void testMultipleMatchAndUnmatch() throws Exception {
        String reconciliationToken = executeUploadAndGetReconciliationToken("ClientMarkoffFile20140113.csv", 304,
                "PaymentologyMarkoffFile20140113.csv", 304);

        mockMvc.perform(
                get(BASE_PATH + reconciliationToken + TRANSACTION_MATCH_SUMMARY_API)
                        .servletPath(BASE_PATH + reconciliationToken + TRANSACTION_MATCH_SUMMARY_API))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("0")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.body.firstTransactionSet.fileName", is("ClientMarkoffFile20140113.csv")))
                .andExpect(jsonPath("$.body.firstTransactionSet.totalUniqueRecords", is(304)))
                .andExpect(jsonPath("$.body.firstTransactionSet.matchedTransactions", is(288)))
                .andExpect(jsonPath("$.body.firstTransactionSet.unmatchedTransactions", is(16)))
                .andExpect(jsonPath("$.body.secondTransactionSet.fileName", is("PaymentologyMarkoffFile20140113.csv")))
                .andExpect(jsonPath("$.body.secondTransactionSet.totalUniqueRecords", is(304)))
                .andExpect(jsonPath("$.body.secondTransactionSet.matchedTransactions", is(288)))
                .andExpect(jsonPath("$.body.secondTransactionSet.unmatchedTransactions", is(16)))
                .andDo(print());
    }
}
