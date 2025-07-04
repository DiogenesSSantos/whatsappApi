package com.github.dio.mensageria.beans;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class BeanGlobalConfig {



    @Bean
    public RestTemplate restTemplate() {
        var simpleClientTemplate = new SimpleClientHttpRequestFactory();

        simpleClientTemplate.setConnectTimeout(Duration.ofMinutes(1));
        simpleClientTemplate.setReadTimeout(Duration.ofDays(2));

        return new RestTemplate();
    }



}
