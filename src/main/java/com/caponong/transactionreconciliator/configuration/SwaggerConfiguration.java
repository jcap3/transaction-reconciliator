package com.caponong.transactionreconciliator.configuration;

import com.caponong.transactionreconciliator.entity.Transaction;
import com.caponong.transactionreconciliator.model.*;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket swaggerApi(TypeResolver typeResolver) {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .protocols(createProtocols())
                .additionalModels(typeResolver.resolve(Response.class),
                        typeResolver.resolve(Transaction.class),
                        typeResolver.resolve(MatchTransactionsCountResponse.class),
                        typeResolver.resolve(ReconciliationRequestDetails.class),
                        typeResolver.resolve(TransactionsUploadResponse.class),
                        typeResolver.resolve(UnmatchedTransactionsResponse.class),
                        typeResolver.resolve(CsvFile.class))
                .apiInfo(apiInfo());
    }

    @SuppressWarnings("deprecation")
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Transaction Reconciliator API",
                "API used to identify matches from a given transactions in csv format",
                "1.0",
                null,
                "Joshua Caponong - joshuapro2@gmail.com",
                null,
                null
        );
    }

    private Set<String> createProtocols() {
        Set<String> protocols = new HashSet<>();
        protocols.add("http");
        protocols.add("https");
        return protocols;
    }
}
