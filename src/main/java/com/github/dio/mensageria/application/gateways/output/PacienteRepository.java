package com.github.dio.mensageria.application.gateways.output;


import com.github.dio.mensageria.domain.paciente.Paciente;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository {

    Paciente salvar(Paciente paciente);

    List<Paciente> buscarTodos();

    Optional<Paciente> buscarPorCodigo(String codigo);

    boolean deletar(String codigo);

    boolean atualizarStatus(String codigo, String status);

}
