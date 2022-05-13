package com.caponong.transactionreconciliator.transaction.matchsummary;

import com.caponong.transactionreconciliator.ApplicationTest;
import com.caponong.transactionreconciliator.enums.ReconciliationRequestStatus;
import com.caponong.transactionreconciliator.error.exception.RequestTokenNotFound;
import com.caponong.transactionreconciliator.model.ReconciliationRequestDetails;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TransactionMatchValidationIT extends ApplicationTest {
    
    @Test
    public void invalidReconciliationTokenTest() throws Exception {
        mockMvc.perform(
                get(BASE_PATH + "@" + TEST_RECONCILIATION_TOKEN + TRANSACTION_MATCH_SUMMARY_API)
                        .servletPath(BASE_PATH + "@" + TEST_RECONCILIATION_TOKEN + TRANSACTION_MATCH_SUMMARY_API))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("999")))
                .andExpect(jsonPath("$.message", is("error")))
                .andExpect(jsonPath("$.errorCode.code", is("003")))
                .andExpect(jsonPath("$.errorCode.message", is("token should only contain numbers, letters, and - with length of 36")))
                .andDo(print());
    }
    
    @Test
    public void requestNotReadyTest() throws Exception {
        
        Mockito.when(mockReconciliationRequestHandlerService.getDetails(TEST_RECONCILIATION_TOKEN))
                .thenReturn(ReconciliationRequestDetails.builder().status(ReconciliationRequestStatus.NOT_READY).build());

        mockMvc.perform(
                get(BASE_PATH + TEST_RECONCILIATION_TOKEN + TRANSACTION_MATCH_SUMMARY_API)
                        .servletPath(BASE_PATH + TEST_RECONCILIATION_TOKEN + TRANSACTION_MATCH_SUMMARY_API))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("999")))
                .andExpect(jsonPath("$.message", is("error")))
                .andExpect(jsonPath("$.errorCode.code", is("004")))
                .andExpect(jsonPath("$.errorCode.message", is("Requested token not yet ready")))
                .andDo(print());
    }
    
    @Test
    public void requestEncounteredAnErrorTest() throws Exception {

        Mockito.when(mockReconciliationRequestHandlerService.getDetails(TEST_RECONCILIATION_TOKEN2))
                .thenReturn(ReconciliationRequestDetails.builder().status(ReconciliationRequestStatus.ERROR).build());

        mockMvc.perform(
                get(BASE_PATH + TEST_RECONCILIATION_TOKEN2 + TRANSACTION_MATCH_SUMMARY_API)
                        .servletPath(BASE_PATH + TEST_RECONCILIATION_TOKEN2 + TRANSACTION_MATCH_SUMMARY_API))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("999")))
                .andExpect(jsonPath("$.message", is("error")))
                .andExpect(jsonPath("$.errorCode.code", is("005")))
                .andExpect(jsonPath("$.errorCode.message", is("Requested token encountered an error")))
                .andDo(print());
    }

    @Test
    public void requestTokenNotFoundTest() throws Exception {
        
        Mockito.when(mockReconciliationRequestHandlerService.getDetails(Mockito.any(String.class)))
                .thenThrow(new RequestTokenNotFound("Request token not found"));
        
        mockMvc.perform(
                get(BASE_PATH + TEST_RECONCILIATION_TOKEN3 + TRANSACTION_MATCH_SUMMARY_API)
                        .servletPath(BASE_PATH + TEST_RECONCILIATION_TOKEN3 + TRANSACTION_MATCH_SUMMARY_API))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("999")))
                .andExpect(jsonPath("$.message", is("error")))
                .andExpect(jsonPath("$.errorCode.code", is("006")))
                .andExpect(jsonPath("$.errorCode.message", is("Request not found")))
                .andDo(print());
    }
}
