package com.github.dio.mensageria.application.gateways.output;


import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository {

    Paciente salvar(Paciente paciente);

    List<Paciente> buscarTodos();

    Page<Paciente> buscarComFiltros(String nome, String bairro, String consultaNome, Consulta.Status status, Pageable pageable);

    Optional<Paciente> buscarPorCodigo(String codigo);

    boolean deletar(String codigo);

    boolean atualizarStatus(String codigo, String status);

}
