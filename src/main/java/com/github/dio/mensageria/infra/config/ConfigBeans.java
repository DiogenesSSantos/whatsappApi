package com.github.dio.mensageria.infra.config;

import com.github.dio.mensageria.application.gateways.PacienteRepository;
import com.github.dio.mensageria.application.usecases.CriarPaciente;
import com.github.dio.mensageria.infra.controller.PacienteControllerMapper;
import com.github.dio.mensageria.infra.gateways.PacienteEntityMapper;
import com.github.dio.mensageria.infra.gateways.PacienteRepositoryJPA;
import com.github.dio.mensageria.infra.persistence.PacienteEntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBeans {

    @Bean
    public PacienteControllerMapper pacienteControllerMapper(){
        return new PacienteControllerMapper();
    }

    @Bean
    public PacienteEntityMapper pacienteEntityMapper(){
        return  new PacienteEntityMapper();
    }

    @Bean
    public PacienteRepositoryJPA pacienteRepositoryJPA (PacienteEntityRepository entityRepository,
                                                        PacienteEntityMapper mapper) {
        return new PacienteRepositoryJPA(entityRepository, mapper);
    }

    @Bean
    public CriarPaciente criarPaciente (PacienteRepository pacienteRepository) {
        return new CriarPaciente(pacienteRepository);
    }

}
