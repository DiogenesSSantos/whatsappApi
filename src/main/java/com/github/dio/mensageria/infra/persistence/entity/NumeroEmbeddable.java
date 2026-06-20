package com.github.dio.mensageria.infra.persistence.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class NumeroEmbeddable {

    private String celular;
    private boolean isWhatsapp;

    public NumeroEmbeddable() {}

    public NumeroEmbeddable(String celular, boolean isWhatsapp) {
        this.celular = celular;
        this.isWhatsapp = isWhatsapp;
    }


    public String getCelular() {
        return celular;
    }

    public boolean isWhatsapp() {
        return isWhatsapp;
    }
}
