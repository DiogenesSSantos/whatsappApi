package com.github.dio.mensageria.infra.controller.pacientecontroller.response;

import com.github.dio.mensageria.infra.controller.pacientecontroller.request.StatusDTORequest;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO de response para dados da consulta")
public record ConsultaDTOResponse(
        @Schema(example = "Consulta Clínica", description = "Nome/tipo da consulta")
        String nome,
        @Schema(example = "2024-07-15T14:30:00", description = "Data e hora do atendimento")
        LocalDateTime dataAtendimento,
        @Schema(example = "2024-06-23T10:00:00", description = "Data e hora da marcação")
        LocalDateTime dataMarcacao,
        @Schema(description = "Status atual da consulta")
        StatusDTORequest status) {
}