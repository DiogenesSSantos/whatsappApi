package com.github.dio.mensageria.infra.controller;

import com.github.dio.mensageria.domain.consulta.Consulta;
import com.github.dio.mensageria.domain.contato.Contato;
import com.github.dio.mensageria.domain.contato.Numero;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.infra.controller.request.ConsultaDTORequest;
import com.github.dio.mensageria.infra.controller.request.ContatoDTORequest;
import com.github.dio.mensageria.infra.controller.request.PacienteDTORequest;
import com.github.dio.mensageria.infra.controller.request.StatusDTORequest;
import com.github.dio.mensageria.infra.controller.response.ConsultaDTOResponse;
import com.github.dio.mensageria.infra.controller.response.ContatoDTOResponse;
import com.github.dio.mensageria.infra.controller.response.NumeroDTOResponse;
import com.github.dio.mensageria.infra.controller.response.PacienteDTOResponse;

import java.util.List;

public class PacienteControllerMapper {


    public Paciente dtoToModel(PacienteDTORequest pacienteDTORequest) {
        return Paciente.builder()
                .nome(pacienteDTORequest.nome())
                .consulta(consultaDTOToConsultaModel(pacienteDTORequest.consulta()))
                .contato(contatoDTOToContatoModel(pacienteDTORequest.contato()))
                .build();

    }


    public PacienteDTOResponse modelToDTO(Paciente paciente) {
        return new PacienteDTOResponse(paciente.getNome(),
                contatoModelToContatoDTO(paciente.getContato()),
                consultaModelToConsultaDTO(paciente.getConsulta()));
    }


    private Consulta consultaDTOToConsultaModel(ConsultaDTORequest consultaDTORequest) {
        return new Consulta(consultaDTORequest.nome(),
                consultaDTORequest.dataAtendimento());

    }

    private Contato contatoDTOToContatoModel(ContatoDTORequest contatoDTORequest) {
        List<Numero> numerosLista = contatoDTORequest.numerosCelular()
                .stream()
                .map(numeroDTORequest -> new Numero(numeroDTORequest.celular()))
                .toList();

        return new Contato(numerosLista, contatoDTORequest.bairro());

    }


    private ConsultaDTOResponse consultaModelToConsultaDTO(Consulta consulta) {
        return new ConsultaDTOResponse(consulta.getNome(),
                consulta.getDataAtendimento(),
                consulta.getDataMarcacao(),
                StatusDTORequest.valueOf(consulta.getStatus().name()));

    }

    private ContatoDTOResponse contatoModelToContatoDTO(Contato contato) {
        List<NumeroDTOResponse> numeroDTOResponseList = contato.getNumerosCelular()
                .stream()
                .map(numero -> new NumeroDTOResponse(numero.getCelular(), numero.isWhatsapp()))
                .toList();

        return new ContatoDTOResponse(numeroDTOResponseList, contato.getBairro());

    }

}
