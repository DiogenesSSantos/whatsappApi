package com.github.dio.mensageria.infra.config;

import com.github.dio.mensageria.application.gateways.output.Mensageria;
import com.github.dio.mensageria.application.gateways.output.OllamaGateway;
import com.github.dio.mensageria.application.gateways.output.PacienteRepository;
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


    @Bean
    public PacienteControllerMapper pacienteControllerMapper() {
        return new PacienteControllerMapper();
    }

    @Bean
    public PacienteEntityMapper pacienteEntityMapper() {
        return new PacienteEntityMapper();
    }

    @Bean
    public PacienteRepositoryJPA pacienteRepositoryJPA(PacienteEntityRepository entityRepository,
                                                       PacienteEntityMapper mapper) {
        return new PacienteRepositoryJPA(entityRepository, mapper);
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
    public OllamaGateway ollamaGateway(){
        return new OllamaHttpGateway();
    }


    @Bean
    public Mensageria mensageria(EvolutionGoClient evolutionGoClient, OllamaGateway ollamaGateway) {
        return new MensageriaN8N(evolutionGoClient, ollamaGateway);
    }

    @Bean
    public NotificarPaciente notificarPaciente(Mensageria mensageria) {
        return new NotificarPaciente(mensageria);
    }

}
