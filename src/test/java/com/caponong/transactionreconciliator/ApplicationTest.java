package com.caponong.transactionreconciliator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public abstract class ApplicationTest {

    @Autowired
    protected MockMvc mockMvc;

    @BeforeClass
    public static void beforeClass() {
    }

    @Before
    public void before() {
    }

    @AfterClass
    public static void afterClass() {
    }

    @After
    public void after() {
    }

}
