package com.caponong.transactionreconciliator;

import com.caponong.transactionreconciliator.entity.Transaction;
import com.caponong.transactionreconciliator.repository.TransactionRepository;
import com.caponong.transactionreconciliator.services.ReconciliationRequestHandlerService;
import com.caponong.transactionreconciliator.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.caponong.transactionreconciliator.constant.TransactionsConstant.FIRST_TRANSACTION_IDENTIFIER;

@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public abstract class ApplicationTest {

    protected static final String BASE_PATH = "/api/transactions/";

    protected static final String TRANSACTION_UPLOAD_API = BASE_PATH + "upload";

    protected static final String TRANSACTION_MATCH_SUMMARY_API = "/matchSummary";

    protected static final String TRANSACTION_UNMATCHED_SUMMARY_API = "/unmatchedTransactionsSummary";

    protected static final String TEST_RECONCILIATION_TOKEN = "7a10fa77-cf77-4e3e-a193-2f75f3c8dace";
    
    protected static final String TEST_RECONCILIATION_TOKEN2 = "18fa8be3-1ec6-4370-b90e-fe4d9179b0b6";
    
    protected static final String TEST_RECONCILIATION_TOKEN3 = "bbbd674e-aaa3-4ff2-a76a-c6295a457eb3";
    
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected TransactionRepository transactionRepository;

    @MockBean(name = "mockReconciliationRequestHandlerService")
    protected ReconciliationRequestHandlerService mockReconciliationRequestHandlerService;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeClass
    public static void beforeClass() {
    }

    @Before
    public void before() {
        Mockito.reset(mockReconciliationRequestHandlerService);
        transactionRepository.deleteAll();
    }

    @AfterClass
    public static void afterClass() {
    }

    @After
    public void after() {
    }

    protected MockMultipartFile getTestDataCsv(String requestPartName, String fileName) throws IOException {
        File file = ResourceUtils.getFile("classpath:testdatacsv/" + fileName);
        return new MockMultipartFile(requestPartName, file.getName(), "text/csv", Files.readAllBytes(file.toPath()));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected void wait(int millis) throws InterruptedException {
        CountDownLatch waiter = new CountDownLatch(1);
        waiter.await(millis, TimeUnit.MILLISECONDS);
    }


}
