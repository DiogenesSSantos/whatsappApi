package com.github.dio.mensageria.infra.controller.request;

public record PacienteDTORequest(String nome, ContatoDTORequest contato, ConsultaDTORequest consulta) {
}
