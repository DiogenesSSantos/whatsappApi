package com.github.dio.mensageria.infra.controller.pacientecontroller.response;

public record PacienteDTOResponse(String nome, ContatoDTOResponse contato, ConsultaDTOResponse consulta) {
}
