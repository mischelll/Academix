package com.academix.curriculumservice.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Value("${api.internalApiKey}")
    private String internalKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return request -> request.header("Authorization", "Bearer " + internalKey);
    }
}