package com.ljy.authority.authority_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AuthorityTestApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AuthorityTestApplication.class, args);
    }
    
}
