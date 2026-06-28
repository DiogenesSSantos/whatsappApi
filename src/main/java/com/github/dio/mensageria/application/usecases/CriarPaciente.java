package com.github.dio.mensageria.application.usecases;

import com.github.dio.mensageria.application.gateways.input.CriarPacienteUseCase;
import com.github.dio.mensageria.application.gateways.output.PacienteRepository;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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

    public Page<Paciente> buscarComFiltros(String nome, String bairro, String consultaNome, Consulta.Status status, Pageable pageable) {
        return pacienteRepository.buscarComFiltros(nome, bairro, consultaNome, status, pageable);
    }

    public Optional<Paciente> buscarPorCodigo(String codigo) {
        return pacienteRepository.buscarPorCodigo(codigo);
    }

    public boolean deletar(String codigo) {
        return pacienteRepository.deletar(codigo);
    }

    public boolean atualizarStatus(String codigo, String status) {
        return pacienteRepository.atualizarStatus(codigo, status);
    }

}
