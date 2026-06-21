package com.github.dio.mensageria.infra.controller.response;

public record PacienteDTOResponse(String nome, ContatoDTOResponse contato, ConsultaDTOResponse consulta) {
}
