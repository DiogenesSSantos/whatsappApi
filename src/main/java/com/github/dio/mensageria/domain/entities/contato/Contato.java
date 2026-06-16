package com.github.dio.mensageria.domain.entities.contato;

public class Contato {

    private String numeroCelular;
    private String bairro;


    public Contato(String numeroCelular, String bairro) {
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
}

