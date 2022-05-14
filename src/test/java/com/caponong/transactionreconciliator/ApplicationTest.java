package com.caponong.transactionreconciliator;

import com.caponong.transactionreconciliator.repository.TransactionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.caponong.transactionreconciliator.constant.TransactionsConstant.FIRST_TRANSACTION_IDENTIFIER;
import static com.caponong.transactionreconciliator.constant.TransactionsConstant.SECOND_TRANSACTION_IDENTIFIER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    protected MockMvc mockMvc;

    @BeforeClass
    public static void beforeClass() {
    }

    @Before
    public void before() {
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
    
    protected String getTestDataString (String fileName) throws IOException {
        File file = ResourceUtils.getFile("classpath:testdataresponse/" + fileName);
        return new String(Files.readAllBytes(file.toPath()));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected void wait(int millis) throws InterruptedException {
        CountDownLatch waiter = new CountDownLatch(1);
        waiter.await(millis, TimeUnit.MILLISECONDS);
    }

    protected String executeUploadAndGetReconciliationToken(String fileName1, int expectedTransactions1, 
                                                            String fileName2, int expectedTransactions2) throws Exception {
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

        waitAllDataToBeInserted(expectedTransactions1, expectedTransactions2);

        JsonNode rootNode = objectMapper.readTree(response);
        return rootNode.get("body").get("reconciliationToken").textValue();
    }

    private void waitAllDataToBeInserted(int expectedTransactions1, int expectedTransactions2) throws InterruptedException {
        int tries = 5;
        do {
            wait(1000);
            if (tries--==0)
                break;
        } while ((expectedTransactions1 != transactionRepository.getTotalUniqueTransactions(FIRST_TRANSACTION_IDENTIFIER + "%") &&
                (expectedTransactions2 != transactionRepository.getTotalUniqueTransactions(SECOND_TRANSACTION_IDENTIFIER + "%"))));
    }

}
