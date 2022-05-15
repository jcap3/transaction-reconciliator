package com.caponong.transactionreconciliator.transaction.matchsummary;

import com.caponong.transactionreconciliator.ApplicationTest;
import com.caponong.transactionreconciliator.enums.ReconciliationRequestStatus;
import com.caponong.transactionreconciliator.error.exception.RequestTokenNotFound;
import com.caponong.transactionreconciliator.model.ReconciliationRequestDetails;
import com.caponong.transactionreconciliator.services.impl.ReconciliationRequestHandlerServiceImpl;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TransactionMatchErrorIT extends ApplicationTest {

    @MockBean
    public ReconciliationRequestHandlerServiceImpl reconciliationRequestHandlerService;

    @After
    public void after() {
        super.after();
        Mockito.reset(reconciliationRequestHandlerService);
    }

    @Test
    public void testInvalidReconciliationToken() throws Exception {
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
    public void testRequestEncounteredAnError() throws Exception {

        Mockito.when(reconciliationRequestHandlerService.getDetails(TEST_RECONCILIATION_TOKEN2))
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
    public void testRequestTokenNotFound() throws Exception {

        Mockito.when(reconciliationRequestHandlerService.getDetails(Mockito.any(String.class)))
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

    @Test
    public void testRequestNotReady() throws Exception {

        Mockito.when(reconciliationRequestHandlerService.getDetails(TEST_RECONCILIATION_TOKEN))
                .thenReturn(ReconciliationRequestDetails.builder().status(ReconciliationRequestStatus.NOT_READY).build());

        mockMvc.perform(
                get(BASE_PATH + TEST_RECONCILIATION_TOKEN + TRANSACTION_MATCH_SUMMARY_API)
                        .servletPath(BASE_PATH + TEST_RECONCILIATION_TOKEN + TRANSACTION_MATCH_SUMMARY_API))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("999")))
                .andExpect(jsonPath("$.message", is("error")))
                .andExpect(jsonPath("$.errorCode.code", is("004")))
                .andExpect(jsonPath("$.errorCode.message", is("Requested token not yet ready")))
                .andDo(print());
    }
}
