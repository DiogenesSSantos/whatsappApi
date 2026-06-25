package com.github.dio.mensageria.infra.controller.pacientecontroller.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de request para criar um novo paciente")
public record PacienteDTORequest(
        @Schema(example = "João Silva", description = "Nome completo do paciente")
        String nome,
        @Schema(description = "Dados de contato do paciente")
        ContatoDTORequest contato,
        @Schema(description = "Informações da consulta agendada")
        ConsultaDTORequest consulta) {
}
