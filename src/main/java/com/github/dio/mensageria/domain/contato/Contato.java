package com.github.dio.mensageria.domain.contato;

import java.util.LinkedList;

public class Contato {

    private LinkedList<Numero> numerosCelular;
    private String bairro;

    public Contato(LinkedList<Numero> numerosCelular, String bairro) {
        this.bairro = bairro;

        if (numerosCelular == null) {
            this.numerosCelular = new LinkedList<>();
            return;
        }

        this.numerosCelular = numerosCelular;
    }


    public void adicionarNumeroCelular(String novoNumero) {
        if (existeNumeroParaContato(novoNumero)) return;

        Numero numero = new Numero(novoNumero);
        this.numerosCelular.add(numero);
    }

    public void atualizarBairro(String novoBairro) {
        this.bairro = novoBairro;
    }

    public void atualizarNumeroParaContato(String numeroAntigo, String numeroNovo) {
        for (Numero numero : numerosCelular) {
            if (numero.getCelular().equals(numeroAntigo)) {
                numero.atualizar(numeroNovo);
                return;
            }
        }
    }

    public boolean existeNumeroParaContato(String numero) {
        return numerosCelular.stream()
                .anyMatch(numeroDaList -> numeroDaList.getCelular().equals(numero));
    }


    public LinkedList<Numero> getNumerosCelular() {
        return numerosCelular;
    }


    public String getBairro() {
        return bairro;
    }

}

