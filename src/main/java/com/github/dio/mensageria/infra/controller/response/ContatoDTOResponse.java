package com.github.dio.mensageria.infra.controller.response;

import com.github.dio.mensageria.infra.controller.request.NumeroDTORequest;

import java.util.LinkedList;
import java.util.List;

public record ContatoDTOResponse(
        List<NumeroDTOResponse> numerosCelular,
                                String bairro) {
}
