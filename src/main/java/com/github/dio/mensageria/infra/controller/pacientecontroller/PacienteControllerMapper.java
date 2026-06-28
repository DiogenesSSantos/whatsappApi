package com.github.dio.mensageria.infra.controller.pacientecontroller;

import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.domain.paciente.contato.Contato;
import com.github.dio.mensageria.domain.paciente.contato.Numero;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.ConsultaDTORequest;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.ContatoDTORequest;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.PacienteDTORequest;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.StatusDTORequest;
import com.github.dio.mensageria.infra.controller.pacientecontroller.response.ConsultaDTOResponse;
import com.github.dio.mensageria.infra.controller.pacientecontroller.response.ContatoDTOResponse;
import com.github.dio.mensageria.infra.controller.pacientecontroller.response.NumeroDTOResponse;
import com.github.dio.mensageria.infra.controller.pacientecontroller.response.PacienteDTOResponse;

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
        return new PacienteDTOResponse(
                paciente.getCodigo(),
                paciente.getNome(),
                contatoModelToContatoDTO(paciente.getContato()),
                consultaModelToConsultaDTO(paciente.getConsulta()));
    }


    public Consulta consultaDTOToConsultaModel(ConsultaDTORequest consultaDTORequest) {
        Consulta consulta = new Consulta(consultaDTORequest.nome(),
                consultaDTORequest.dataAtendimento());

        if (consultaDTORequest.status() != null) {
            consulta.atualizarStatus(
                    Consulta.Status.valueOf(consultaDTORequest.status().name()));
        }

        return consulta;
    }

    public Contato contatoDTOToContatoModel(ContatoDTORequest contatoDTORequest) {
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
