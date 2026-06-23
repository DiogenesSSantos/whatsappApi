package com.github.dio.mensageria.infra.controller.pacientecontroller.request;

import java.util.LinkedList;

public record ContatoDTORequest(
        LinkedList<NumeroDTORequest> numerosCelular,
                                String bairro) {
}
