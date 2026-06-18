package com.github.dio.mensageria.domain.entities.contato;

public class Numero {
    private String numeroCelular;
    private boolean isWhatsapp;


    public Numero(String numeroCelular) {
        validaNumeroCelular(numeroCelular);
        this.numeroCelular = numeroCelular;
        this.isWhatsapp = true;
    }

    public void naoPossuiWhatsapp () {
        this.isWhatsapp = false;
    }

    public void atualizar(String numeroNovo) {
        validaNumeroCelular(numeroCelular);
        this.numeroCelular = numeroNovo;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }
    public boolean isWhatsapp() {
        return isWhatsapp;
    }

    private void validaNumeroCelular(String numeroCelular) {
        if (numeroCelular == null || numeroCelular.matches("^55[1-9][0-9](?:9\\d{8}|\\d{8})$")) {
            throw new CelularInvalidoException(
                    String.format("O numero {%s} inválido, deve ser apenas dígitos no padrão: DDDnumero.",
                            numeroCelular));
        }
    }
}
