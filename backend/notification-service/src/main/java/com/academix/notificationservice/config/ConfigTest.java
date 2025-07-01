package com.academix.notificationservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigTest implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigTest.class);
    
    @Value("${services.user.url:http://localhost:8081}")
    private String userServiceUrl;
    
    @Value("${api.internalApiKey:internal-secret-key}")
    private String internalApiKey;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("=== Configuration Test ===");
        logger.info("userServiceUrl: {}", userServiceUrl);
        logger.info("internalApiKey: {}", internalApiKey != null ? 
            internalApiKey.substring(0, Math.min(10, internalApiKey.length())) + "..." : "null");
        logger.info("internalApiKey length: {}", internalApiKey != null ? internalApiKey.length() : "null");
        logger.info("==========================");
    }
} 