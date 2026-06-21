package com.github.dio.mensageria.infra.controller.request;

import com.github.dio.mensageria.domain.contato.Numero;

import java.util.LinkedList;

public record ContatoDTORequest(
        LinkedList<NumeroDTORequest> numerosCelular,
                                String bairro) {
}
