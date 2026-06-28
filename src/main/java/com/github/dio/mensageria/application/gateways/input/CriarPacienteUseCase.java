package com.github.dio.mensageria.application.gateways.input;

import com.github.dio.mensageria.domain.paciente.Paciente;

import java.util.Optional;

public interface CriarPacienteUseCase {

    Paciente cadastrarPaciente(Paciente paciente);

    Optional<Paciente> buscarPorCodigo(String codigo);

    boolean deletar(String codigo);

    boolean atualizarStatus(String codigo, String status);

}
