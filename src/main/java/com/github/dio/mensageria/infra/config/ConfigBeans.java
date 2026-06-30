package com.github.dio.mensageria.infra.config;

import com.github.dio.mensageria.application.gateways.output.Mensageria;
import com.github.dio.mensageria.application.gateways.output.OllamaGateway;
import com.github.dio.mensageria.application.gateways.output.PacienteRepository;
import com.github.dio.mensageria.application.services.FilaMensagem;
import com.github.dio.mensageria.application.usecases.CriarPaciente;
import com.github.dio.mensageria.application.usecases.NotificarPaciente;
import com.github.dio.mensageria.infra.controller.pacientecontroller.PacienteControllerMapper;
import com.github.dio.mensageria.infra.gateways.*;
import com.github.dio.mensageria.infra.persistence.PacienteEntityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigBeans {

    private static final long DEFAULT_DELAY_ENTRE_PACIENTES_MS = 60000;

    @Bean
    public PacienteControllerMapper pacienteControllerMapper() {
        return new PacienteControllerMapper();
    }

    @Bean
    public PacienteEntityMapper pacienteEntityMapper() {
        return new PacienteEntityMapper();
    }

    @Bean
    public PacienteRepositoryJPAGateway pacienteRepositoryJPA(PacienteEntityRepository entityRepository,
                                                              PacienteEntityMapper mapper) {
        return new PacienteRepositoryJPAGateway(entityRepository, mapper);
    }

    @Bean
    public CriarPaciente criarPaciente(PacienteRepository pacienteRepository) {
        return new CriarPaciente(pacienteRepository);
    }

    @Bean
    public RestTemplate restTemplateN8N() {
        return new RestTemplate();
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .build();
    }

    @Bean
    public OllamaGateway ollamaGateway(@Value("${ollama.url}") String ollamaUrl,
                                       @Value("${ollama.model}") String modelName) {
        return new OllamaHttpGateway(ollamaUrl, modelName);
    }

    @Bean
    public Mensageria mensageria(EvolutionGoClient evolutionGoClient, OllamaGateway ollamaGateway) {
        return new MensageriaImplGateway(evolutionGoClient, ollamaGateway);
    }

    @Bean
    public FilaMensagem filaMensagem(Mensageria mensageria,
                                     @Value("${fila.delay-ms:60000}") long delayMs) {
        return new FilaMensagem(mensageria, delayMs);
    }

    @Bean
    public NotificarPaciente notificarPaciente(Mensageria mensageria, FilaMensagem filaMensagem) {
        return new NotificarPaciente(mensageria, filaMensagem);
    }

}
