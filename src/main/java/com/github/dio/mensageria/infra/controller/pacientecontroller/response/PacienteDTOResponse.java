package com.github.dio.mensageria.infra.controller.pacientecontroller.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de response com dados do paciente criado")
public record PacienteDTOResponse(
        @Schema(example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890", description = "Código UUID do paciente")
        String codigo,
        @Schema(example = "João Silva", description = "Nome do paciente")
        String nome,
        @Schema(description = "Dados de contato do paciente")
        ContatoDTOResponse contato,
        @Schema(description = "Informações da consulta")
        ConsultaDTOResponse consulta) {
}
