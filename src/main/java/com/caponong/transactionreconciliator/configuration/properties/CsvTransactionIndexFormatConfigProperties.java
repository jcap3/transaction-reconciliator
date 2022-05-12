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
@ConfigurationProperties("csv.transaction.index")
public class CsvTransactionIndexFormatConfigProperties {

    private int profileName;
    
    private int transactionDate;
    
    private int transactionAmount;
    
    private int transactionNarrative;
    
    private int transactionDescription;
    
    private int transactionId;
    
    private int transactionType;
    
    private int walletReference;
}
