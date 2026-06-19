package com.github.dio.mensageria.infra.gateways;

import com.github.dio.mensageria.application.gateways.PacienteRepository;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.infra.persistence.PacienteEntityRepository;

public class PacienteRepositoryJPA implements PacienteRepository {

    private final PacienteEntityRepository pacienteEntityRepository;
    private final PacienteEntityMapper mapper;

    public PacienteRepositoryJPA(PacienteEntityRepository pacienteEntityRepository, PacienteEntityMapper mapper) {
        this.pacienteEntityRepository = pacienteEntityRepository;
        this.mapper = mapper;
    }


    @Override
    public Paciente salvar(Paciente paciente) {
        var pacienteConvertido = mapper.modelToEntity(paciente);
        var pacienteBD = pacienteEntityRepository.save(pacienteConvertido);
        return mapper.entityToModel(pacienteBD);
    }


}
