package com.caponong.transactionreconciliator.transaction.unmatchsummary;

import com.caponong.transactionreconciliator.ApplicationTest;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UnmatchedTransactionSuccessIT extends ApplicationTest {

    @Test
    public void testSuccessReconciliationOfUnmatchedTransactions() throws Exception {

        String reconciliationToken = executeUploadAndGetReconciliationToken("testDataCsvOneMatch_FirstFile.csv",
                "testDataCsvOneMatch_SecondFile.csv");

        mockMvc.perform(
                get(BASE_PATH + reconciliationToken + TRANSACTION_UNMATCHED_SUMMARY_API)
                        .servletPath(BASE_PATH + reconciliationToken + TRANSACTION_UNMATCHED_SUMMARY_API))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("0")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.body.firstTransactionSet.fileName", is("testDataCsvOneMatch_FirstFile.csv")))
                .andExpect(jsonPath("$.body.firstTransactionSet.unmatchedTransactions", hasSize(3)))
                .andExpect(jsonPath("$.body.secondTransactionSet.fileName", is("testDataCsvOneMatch_SecondFile.csv")))
                .andExpect(jsonPath("$.body.secondTransactionSet.unmatchedTransactions", hasSize(3)))
                .andDo(print());
    }

    @Test
    public void testSuccessWithEmptyFields() throws Exception {
        String reconciliationToken = executeUploadAndGetReconciliationToken("testDataCsvUnequalTotalUniqueRowsChunked_FirstFile.csv",
                "testDataCsvUnequalTotalUniqueRowsChunked_SecondFile.csv");

        mockMvc.perform(
                get(BASE_PATH + reconciliationToken + TRANSACTION_UNMATCHED_SUMMARY_API)
                        .servletPath(BASE_PATH + reconciliationToken + TRANSACTION_UNMATCHED_SUMMARY_API))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("0")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(content().json(getTestDataString("testDataCsvUnequalTotalUniqueRowsChunked.json")))
                .andDo(print());
    }
    
    @Test
    public void testSuccessChunkedMultiplePotentialMatch() throws Exception {
        String reconciliationToken = executeUploadAndGetReconciliationToken("ClientMarkoffFile20140113.csv",
                "PaymentologyMarkoffFile20140113.csv");

        mockMvc.perform(
                get(BASE_PATH + reconciliationToken + TRANSACTION_UNMATCHED_SUMMARY_API)
                        .servletPath(BASE_PATH + reconciliationToken + TRANSACTION_UNMATCHED_SUMMARY_API))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("0")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(content().json(getTestDataString("testDataClientMarkOffAndPaymentologyMarkOff.json")))
                .andDo(print());
    }
}
