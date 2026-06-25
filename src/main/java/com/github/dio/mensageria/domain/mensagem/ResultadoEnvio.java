package com.github.dio.mensageria.domain.mensagem;

public sealed interface ResultadoEnvio
        permits ResultadoEnvio.Sucesso, ResultadoEnvio.Falha {

    record Sucesso() implements ResultadoEnvio {
    }

    record Falha(String motivo) implements ResultadoEnvio {
    }
}