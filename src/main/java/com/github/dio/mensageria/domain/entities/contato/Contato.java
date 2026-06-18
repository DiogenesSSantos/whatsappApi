package com.github.dio.mensageria.domain.entities.contato;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Pattern;

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

    public void atualizarBairro(String novoBairro) {
        this.bairro = novoBairro;
    }

    public void atualizarNumeroParaContato (String numeroAntigo, String numeroNovo) {
        int indexNumero = numerosCelular.indexOf(numeroAntigo);
        if (indexNumero == -1) {
            numerosCelular.add(new Numero(numeroNovo));
            return;
        }
        numerosCelular.get(indexNumero).atualizar(numeroNovo);
    }

    public LinkedList<Numero> getNumerosCelular() {
        return numerosCelular;
    }

    public boolean existeNumeroParaContato(String numero) {
        return numerosCelular.contains(numero);
    }


    public String getBairro() {
        return bairro;
    }

}

