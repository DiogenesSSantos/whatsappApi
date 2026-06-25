package com.github.dio.mensageria.infra.controller.pacientecontroller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO de response para dados de contato")
public record ContatoDTOResponse(
        @Schema(description = "Lista de números de celular do paciente")
        List<NumeroDTOResponse> numerosCelular,
        @Schema(example = "Centro", description = "Bairro de localização")
        String bairro) {
}
