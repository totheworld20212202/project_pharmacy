package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;

@EnableRetry
@Configuration
public class RetryConfig {
//    // retry Template방법도 있고, @EnableRetry 방법도 있는데, 밑에는 retry Template을 사용하는방법이다.
//    @Bean
//    public RetryTemplate retryTemplate(){
//        return new RetryTemplate();
//    }
}
