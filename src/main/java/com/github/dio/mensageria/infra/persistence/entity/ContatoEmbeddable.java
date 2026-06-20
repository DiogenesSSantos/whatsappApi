package com.github.dio.mensageria.infra.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;

import java.util.ArrayList;
import java.util.List;

@Embeddable
public class ContatoEmbeddable {

    @ElementCollection
    @CollectionTable(name = "paciente_telefones", joinColumns = @JoinColumn(name = "paciente_id"))
    private List<NumeroEmbeddable> numerosCelular = new ArrayList<>();
    private String bairro;

    public ContatoEmbeddable() {
    }

    public ContatoEmbeddable(List<NumeroEmbeddable> numerosCelular, String bairro) {
        this.numerosCelular = numerosCelular;
        this.bairro = bairro;
    }

    public List<NumeroEmbeddable> getNumerosCelular() {
        return numerosCelular;
    }

    public String getBairro() {
        return bairro;
    }
}
