package com.caponong.transactionreconciliator.configuration.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties("csv.transaction.headername")
public class CsvTransactionHeaderNames {

    private String profileName;
    
    private String transactionDate;
    
    private String transactionAmount;
    
    private String transactionNarrative;
    
    private String transactionDescription;
    
    private String transactionId;
    
    private String transactionType;
    
    private String walletReference;
}
