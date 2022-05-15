package com.caponong.transactionreconciliator.configuration;

import com.caponong.transactionreconciliator.entity.Transaction;
import com.caponong.transactionreconciliator.model.*;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket swaggerApi(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.caponong.transactionreconciliator.controller"))
                .paths(PathSelectors.any())
                .build()
                .protocols(Sets.newHashSet("http", "https"))
                .additionalModels(typeResolver.resolve(Response.class),
                        typeResolver.resolve(Transaction.class),
                        typeResolver.resolve(MatchTransactionsCountResponse.class),
                        typeResolver.resolve(ReconciliationRequestDetails.class),
                        typeResolver.resolve(TransactionsUploadResponse.class),
                        typeResolver.resolve(UnmatchedTransactionsResponse.class),
                        typeResolver.resolve(CsvFile.class))
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Transaction Reconciliator API",
                "API used to identify matches from a given transactions in csv format",
                "1.0",
                null,
                new Contact("Joshua Caponong", null, "joshuapro2@gmail.com"),
                null,
                null
        );
    }
}
