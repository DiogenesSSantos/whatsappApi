package com.github.dio.mensageria.infra.gateways;

import com.github.dio.mensageria.application.gateways.output.PacienteRepository;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.infra.persistence.PacienteEntityRepository;
import com.github.dio.mensageria.infra.persistence.entity.PacienteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PacienteRepositoryJPAGateway implements PacienteRepository {

    private final PacienteEntityRepository pacienteEntityRepository;
    private final PacienteEntityMapper mapper;

    public PacienteRepositoryJPAGateway(PacienteEntityRepository pacienteEntityRepository, PacienteEntityMapper mapper) {
        this.pacienteEntityRepository = pacienteEntityRepository;
        this.mapper = mapper;
    }

    @Override
    public Paciente salvar(Paciente paciente) {
        var existente = pacienteEntityRepository.findByCodigo(paciente.getCodigo());

        if (existente.isPresent()) {
            var entity = existente.get();
            entity = mapper.atualizarEntity(entity, paciente);
            var pacienteBD = pacienteEntityRepository.save(entity);
            return mapper.entityToModel(pacienteBD);
        }

        var pacienteConvertido = mapper.modelToEntity(paciente);
        var pacienteBD = pacienteEntityRepository.save(pacienteConvertido);
        return mapper.entityToModel(pacienteBD);
    }

    @Override
    public List<Paciente> buscarTodos() {
        List<PacienteEntity> pacientesBD = pacienteEntityRepository.findAll();
        return pacientesBD.stream()
                .map(mapper::entityToModel)
                .toList();
    }

    @Override
    public Page<Paciente> buscarComFiltros(String nome, String bairro, String consultaNome, Consulta.Status status,
                                          LocalDateTime dataMarcacaoInicio, LocalDateTime dataAtendimentoInicio, Pageable pageable) {
        return pacienteEntityRepository.buscarComFiltros(nome, bairro, consultaNome, status, dataMarcacaoInicio, dataAtendimentoInicio, pageable)
                .map(mapper::entityToModel);
    }

    @Override
    public Optional<Paciente> buscarPorCodigo(String codigo) {
        return pacienteEntityRepository.findByCodigo(codigo)
                .map(mapper::entityToModel);
    }

    @Override
    public boolean deletar(String codigo) {
        return pacienteEntityRepository.deleteByCodigo(codigo) > 0;
    }

    @Override
    public boolean atualizarStatus(String codigo, String status) {
        return pacienteEntityRepository.atualizarStatusByCodigo(codigo, status) > 0;
    }

}
