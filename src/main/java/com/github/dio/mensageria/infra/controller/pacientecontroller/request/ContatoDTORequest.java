package com.github.dio.mensageria.infra.controller.pacientecontroller.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de request para dados de contato do paciente")
public record ContatoDTORequest(
        @Schema(description = "Lista de números de celular do paciente")
        List<NumeroDTORequest> numerosCelular,
        @Schema(example = "Centro", description = "Bairro de localização do paciente")
        String bairro) {
}
