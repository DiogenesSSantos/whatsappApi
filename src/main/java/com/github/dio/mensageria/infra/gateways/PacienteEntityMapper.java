package com.github.dio.mensageria.infra.gateways;

import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.infra.persistence.PacienteEntity;

public class PacienteEntityMapper {


    public PacienteEntity modelToEntity(Paciente paciente) {
        return new PacienteEntity(paciente.getCodigo(),
                paciente.getNome(), paciente.getContato(), paciente.getConsulta());

    }

    public Paciente entityToModel(PacienteEntity pacienteEntity) {
        return Paciente.builder()
                .codigo(pacienteEntity.getCodigo())
                .nome(pacienteEntity.getNome())
                .contato(pacienteEntity.getContato())
                .consulta(pacienteEntity.getConsulta())
                .build();
    }


}
