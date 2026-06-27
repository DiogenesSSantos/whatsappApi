package com.github.dio.mensageria.infra.gateways;

import com.github.dio.mensageria.infra.config.EvolutionGoConfig;
import com.github.dio.mensageria.infra.gateways.dto.SendTextRequest;
import com.github.dio.mensageria.infra.gateways.dto.SendTextResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvolutionGoClient {

    private final EvolutionGoConfig config;
    private final RestClient restClient;

    public SendTextResponse enviarTexto(String numero, String texto) {
        SendTextRequest body = SendTextRequest.builder()
                .number(numero)
                .text(texto)
                .build();

        try {
            return restClient.post()
                    .uri(config.getBaseUrl() + "/send/text")
                    .header("apikey", config.getApiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(SendTextResponse.class);
        } catch (HttpClientErrorException e) {
            log.error("Erro ao enviar mensagem WhatsApp. Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new IllegalArgumentException("Falha ao enviar mensagem para " + numero, e);
        }
    }
}