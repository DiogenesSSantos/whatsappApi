package com.github.dio.mensageria.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class N8NService {
    private static final String URL_N8N = "https://n8n.devdiogenes.shop/webhook-test/api-java";
    private static final Logger log = LoggerFactory.getLogger(N8NService.class);
    private  final  RestTemplate restTemplate;


    public N8NService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> enviarPayload(String nomeCliente , String numero, String mensagem) {
        log.warn("ENVIANDO MENSAGEM PARA N8N");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        Map<String , Object> jsonCorpo = Map.of(
                "nome", nomeCliente,
                "telefones", numero,
                "mensagem", mensagem
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(jsonCorpo, headers);


        return restTemplate.exchange(
                URL_N8N, HttpMethod.POST,request,String.class);
    }


}
