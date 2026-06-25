package com.github.dio.mensageria.infra.controller.pacientecontroller.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de response com dados do paciente criado")
public record PacienteDTOResponse(
        @Schema(example = "João Silva", description = "Nome do paciente")
        String nome,
        @Schema(description = "Dados de contato do paciente")
        ContatoDTOResponse contato,
        @Schema(description = "Informações da consulta")
        ConsultaDTOResponse consulta) {
}
