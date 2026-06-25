package com.github.dio.mensageria.infra.controller.pacientecontroller.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status da consulta", example = "MARCADO")
public enum StatusDTORequest {
    @Schema(description = "Consulta marcada")
    MARCADO,
    @Schema(description = "Aguardando confirmação")
    AGUARDANDO,
    @Schema(description = "Paciente não possui WhatsApp")
    NAO_POSSUI_WHATSAPP,
    @Schema(description = "Consulta rejeitada")
    REJEITADO
}
