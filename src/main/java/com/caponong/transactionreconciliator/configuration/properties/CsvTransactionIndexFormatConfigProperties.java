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

    private Integer profileName;
    
    private Integer transactionDate;
    
    private Integer transactionAmount;
    
    private Integer transactionNarrative;
    
    private Integer transactionDescription;
    
    private Integer transactionId;
    
    private Integer transactionType;
    
    private Integer walletReference;
}
