package com.github.dio.mensageria.infra.controller.pacientecontroller.response;

import java.util.List;

public record ContatoDTOResponse(
        List<NumeroDTOResponse> numerosCelular,
                                String bairro) {
}
