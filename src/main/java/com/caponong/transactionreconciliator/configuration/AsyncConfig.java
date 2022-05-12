package com.caponong.transactionreconciliator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    
    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        return createExecutor(2);
    }
    
    @Bean (name = "expiredTokensExecutor")
    public Executor expiredTokensExecutor() {
        return createExecutor(2);
    }
    
    private ThreadPoolTaskExecutor createExecutor (int poolSize) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(poolSize);
        return threadPoolTaskExecutor;
    }
}
