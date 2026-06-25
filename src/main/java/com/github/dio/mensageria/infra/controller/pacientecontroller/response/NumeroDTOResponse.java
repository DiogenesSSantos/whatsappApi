package com.github.dio.mensageria.infra.controller.pacientecontroller.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de response para número de celular")
public record NumeroDTOResponse(
        @Schema(example = "11987654321", description = "Número do celular com DDD")
        String celular,
        @Schema(example = "true", description = "Indicador se tem WhatsApp")
        boolean isWhatsapp) {
}
