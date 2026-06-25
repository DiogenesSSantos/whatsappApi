package com.github.dio.mensageria.infra.gateways;

import com.github.dio.mensageria.application.gateways.output.Mensageria;
import com.github.dio.mensageria.domain.mensagem.ResultadoEnvio;
import com.github.dio.mensageria.domain.paciente.Paciente;
import org.springframework.web.client.RestTemplate;

public class MensageriaN8N implements Mensageria {

    private final RestTemplate restTemplateN8N;

    public MensageriaN8N(RestTemplate restTemplateN8N) {
        this.restTemplateN8N = restTemplateN8N;
    }

    @Override
    public ResultadoEnvio enviar(Paciente paciente) {
        return null;
    }
}
