package com.github.dio.messageira.model;

import java.time.LocalDateTime;

public class Filtro {

    private String nome;
    private String bairro;
    private LocalDateTime dataMarcacaoInicial;
    private LocalDateTime dataMarcacaoFinal;
    private String tipoConsulta;
    private String motivo;


    public Filtro(String nome, String bairro, LocalDateTime dataMarcacaoInicial, LocalDateTime dataMarcacaoFinal, String tipoConsulta, String motivo) {
        this.nome = nome;
        this.bairro = bairro;
        this.dataMarcacaoInicial = dataMarcacaoInicial;
        this.dataMarcacaoFinal = dataMarcacaoFinal;
        this.tipoConsulta = tipoConsulta;
        this.motivo = motivo;
    }


    public String getNome() {
        return nome;
    }

    public String getBairro() {
        return bairro;
    }

    public LocalDateTime getDataMarcacaoInicial() {
        return dataMarcacaoInicial;
    }

    public LocalDateTime getDataMarcacaoFinal() {
        return dataMarcacaoFinal;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public String getMotivo() {
        return motivo;
    }
}
