package com.github.dio.mensageria.application.usecases;

public class ResultadoEnvio {
    private final boolean isEnviado;

    private ResultadoEnvio(boolean isEnviado) {
        this.isEnviado = isEnviado;
    }

    public static ResultadoEnvio sucesso() {
        return new ResultadoEnvio(true);
    }

    public static ResultadoEnvio falha() {
        return new ResultadoEnvio(false);
    }

}
