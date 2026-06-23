package com.github.dio.mensageria.infra.controller.pacientecontroller.request;

public record PacienteDTORequest(String nome, ContatoDTORequest contato, ConsultaDTORequest consulta) {
}
