package com.github.dio.mensageria.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "evolution.go")
@Data
public class EvolutionGoConfig {
    private String baseUrl;
    private String apiKey;
    private String instance;
}