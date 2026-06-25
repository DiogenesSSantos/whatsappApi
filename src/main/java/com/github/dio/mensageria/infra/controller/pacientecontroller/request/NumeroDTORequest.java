package com.github.dio.mensageria.infra.controller.pacientecontroller.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de request para número de celular")
public record NumeroDTORequest(
        @Schema(example = "11987654321", description = "Número do celular com DDD")
        String celular,
        @Schema(example = "true", description = "Indicador se o número tem WhatsApp")
        boolean isWhatsapp) {
}
