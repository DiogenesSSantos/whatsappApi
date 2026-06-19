package com.github.dio.mensageria.domain.entities.contato;

import java.util.Objects;

public class Numero  {
    private String celular;
    private boolean isWhatsapp;


    public Numero(String celular) {
        validaNumeroCelular(celular);
        this.celular = celular;
        this.isWhatsapp = true;
    }

    public void naoPossuiWhatsapp () {
        this.isWhatsapp = false;
    }

    public void atualizar(String numeroNovo) {
        validaNumeroCelular(numeroNovo);
        this.celular = numeroNovo;
    }

    public String getCelular() {
        return celular;
    }
    public boolean isWhatsapp() {
        return isWhatsapp;
    }


    private void validaNumeroCelular(String numeroCelular) {
        if (numeroCelular == null || !numeroCelular.matches("^55[1-9][0-9](?:9\\d{8}|\\d{8})$")) {
            throw new CelularInvalidoException(
                    String.format("O numero {%s} inválido, deve ser apenas dígitos no padrão: CÓDIGO+DDD+NUMERO "+
                                    "5581988008800.",
                            numeroCelular));
        }
    }


}
