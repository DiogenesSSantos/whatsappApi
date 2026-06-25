package com.github.dio.mensageria.infra.gateways;

import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.domain.paciente.contato.Contato;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.infra.persistence.entity.ConsultaEmbeddable;
import com.github.dio.mensageria.infra.persistence.entity.ContatoEmbeddable;
import com.github.dio.mensageria.infra.persistence.entity.NumeroEmbeddable;
import com.github.dio.mensageria.infra.persistence.entity.PacienteEntity;

import java.util.ArrayList;

public class PacienteEntityMapper {


    public PacienteEntity modelToEntity(Paciente paciente) {
        return new PacienteEntity(paciente.getCodigo(),
                paciente.getNome(),
                contatoToContatoEntity(paciente.getContato()),
                consultaToConsultaEmbeddable(paciente.getConsulta()));
    }

    public Paciente entityToModel(PacienteEntity pacienteEntity) {
        return Paciente.builder()
                .codigo(pacienteEntity.getCodigo())
                .nome(pacienteEntity.getNome())
                .consulta(consultaEntityToConsultaModel(pacienteEntity.getConsulta()))
                .contato(contatoToContatoEntity(pacienteEntity.getContato()))
                .build();

    }


    private Consulta consultaEntityToConsultaModel(ConsultaEmbeddable consultaEmbeddable) {
        var consulta = new Consulta();
        consulta.atualizarNome(consultaEmbeddable.getNome());
        consulta.atualizarDataAtendimento(consultaEmbeddable.getDataAtendimento());
        consulta.atualizarDataMarcacao(consultaEmbeddable.getDataMarcacao());
        consulta.atualizarStatus(consultaEmbeddable.getStatus());

        return consulta;
    }

    private ConsultaEmbeddable consultaToConsultaEmbeddable(Consulta consulta) {
        ConsultaEmbeddable consultaEmbeddable = new ConsultaEmbeddable();
        consultaEmbeddable.setNome(consulta.getNome());
        consultaEmbeddable.setDataAtendimento(consulta.getDataAtendimento());
        consultaEmbeddable.setDataMarcacao(consulta.getDataMarcacao());
        consultaEmbeddable.setStatus(consulta.getStatus());

        return consultaEmbeddable;
    }


    private Contato contatoToContatoEntity(ContatoEmbeddable contatoEmbeddable) {
        var contato = new Contato(new ArrayList<>(), contatoEmbeddable.getBairro());
        contatoEmbeddable.getNumerosCelular().forEach(numeroEmbeddable ->
                contato.adicionarNumeroCelular(numeroEmbeddable.getCelular()));

        return contato;
    }

    private ContatoEmbeddable contatoToContatoEntity(Contato contato) {
        ContatoEmbeddable contatoEmbeddable = new ContatoEmbeddable(new ArrayList<>(), contato.getBairro());
        contato.getNumerosCelular().forEach(numero ->
                contatoEmbeddable.getNumerosCelular().add(new NumeroEmbeddable(numero.getCelular(),
                        numero.isWhatsapp())));


        return contatoEmbeddable;
    }

}
