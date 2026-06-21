package com.github.dio.mensageria.application.gateways;


import com.github.dio.mensageria.domain.paciente.Paciente;

import java.util.List;

public interface PacienteRepository {

    Paciente salvar(Paciente paciente);

    List<Paciente> buscarTodos();

}
