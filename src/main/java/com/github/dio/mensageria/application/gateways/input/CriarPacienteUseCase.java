package com.github.dio.mensageria.application.gateways.input;

import com.github.dio.mensageria.domain.paciente.Paciente;

public interface CriarPacienteUseCase {

    Paciente cadastrarPaciente(Paciente paciente);

}
