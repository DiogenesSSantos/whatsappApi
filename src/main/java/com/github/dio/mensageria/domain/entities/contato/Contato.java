package com.github.dio.mensageria.domain.entities.contato;

import java.util.regex.Pattern;

public class Contato {
    private static final Pattern TELEFONE_BRASIL = Pattern.compile("^55[1-9][0-9](?:9\\d{8}|\\d{8})$");

    private String numeroCelular;
    private String bairro;


    public Contato(String numeroCelular, String bairro) {
        if (!validaNumeroTelefone(numeroCelular)) {
            throw new CelularInvalidoException(
                    String.format("O numero {%s} inválido, deve ser apenas dígitos no padrão: 55DDDnumero.",
                            numeroCelular));
        }
        this.numeroCelular = numeroCelular;
        this.bairro = bairro;
    }


    public void atualizarNumero(String novoNumero) {
        this.numeroCelular = novoNumero;
    }

    public void atualizarBairro(String novoBairro) {
        this.bairro = novoBairro;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public String getBairro() {
        return bairro;
    }

    public boolean validaNumeroTelefone(String numeroCelular) {
        if (numeroCelular == null) return false;
        String trimmed = numeroCelular.trim();
        return TELEFONE_BRASIL.matcher(trimmed).matches();
    }
}

