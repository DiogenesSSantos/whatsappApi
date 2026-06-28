package com.github.dio.mensageria.application.usecases;

import com.github.dio.mensageria.application.gateways.input.CriarPacienteUseCase;
import com.github.dio.mensageria.application.gateways.output.PacienteRepository;
import com.github.dio.mensageria.domain.paciente.Paciente;

import java.util.List;

public class CriarPaciente implements CriarPacienteUseCase {

    private final PacienteRepository pacienteRepository;

    public CriarPaciente(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente cadastrarPaciente(Paciente paciente){
        return pacienteRepository.salvar(paciente);
    }

    public List<Paciente> buscarTodos() {
        return pacienteRepository.buscarTodos();
    }

}
