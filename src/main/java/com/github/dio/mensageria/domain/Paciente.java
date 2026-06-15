package com.github.dio.mensageria.domain;

import java.time.LocalDateTime;

public class Paciente {

    private String codigo;
    private String nome;
    private String numero;
    private String bairro;
    private String consulta;
    private String dataConsulta;
    private LocalDateTime dataMarcacao = LocalDateTime.now();
    private String motivo;



    






    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public String getConsulta() {
        return consulta;
    }

    public String getDataConsulta() {
        return dataConsulta;
    }

    public LocalDateTime getDataMarcacao() {
        return dataMarcacao;
    }

    public String getMotivo() {
        return motivo;
    }
}
