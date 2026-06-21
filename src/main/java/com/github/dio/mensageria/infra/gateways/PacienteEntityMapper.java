package com.github.dio.mensageria.infra.gateways;

import com.github.dio.mensageria.domain.consulta.Consulta;
import com.github.dio.mensageria.domain.contato.Contato;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.infra.persistence.entity.ConsultaEmbeddable;
import com.github.dio.mensageria.infra.persistence.entity.ContatoEmbeddable;
import com.github.dio.mensageria.infra.persistence.entity.PacienteEntity;

import java.util.ArrayList;

public class PacienteEntityMapper {


    public PacienteEntity modelToEntity(Paciente paciente) {
        return null;

    }

    public Paciente entityToModel(PacienteEntity pacienteEntity) {
        return Paciente.builder()
                .codigo(pacienteEntity.getCodigo())
                .nome(pacienteEntity.getNome())
                .consulta(consultaEntityToConsultaModel(pacienteEntity.getConsulta()))
                .contato(contatoEntityToContatoModel(pacienteEntity.getContato()))
                .build();

    }


    private Consulta consultaEntityToConsultaModel(ConsultaEmbeddable consultaEmbeddable) {
        var consulta = new Consulta();
        consulta.setNome(consultaEmbeddable.getNome());
        consulta.setDataAtendimento(consultaEmbeddable.getDataAtendimento());
        consulta.setDataMarcacao(consultaEmbeddable.getDataMarcacao());
        consulta.setStatus(consultaEmbeddable.getStatus());

        return consulta;
    }


    private Contato contatoEntityToContatoModel(ContatoEmbeddable contatoEmbeddable) {
        var contato = new Contato(new ArrayList<>(), contatoEmbeddable.getBairro());
        contatoEmbeddable.getNumerosCelular().forEach(numeroEmbeddable ->
                contato.adicionarNumeroCelular(numeroEmbeddable.getCelular()));

        return contato;
    }
}
