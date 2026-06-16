package com.github.dio.mensageria.domain.entities.paciente;

import com.github.dio.mensageria.domain.entities.consulta.Consulta;
import com.github.dio.mensageria.domain.entities.contato.Contato;

import java.util.UUID;

public class Paciente {

    private String codigo;
    private String nome;
    private Contato contato;
    private Consulta consulta;

    public Paciente() {
    }

    public Paciente(String nome, Contato contato, Consulta consulta) {
        this.codigo = gerarCodigo();
        this.nome = nome;
        this.contato = contato;
        this.consulta = consulta;

    }


    private static String gerarCodigo() {
        return UUID.randomUUID().toString();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public Contato getContato() {
        return contato;
    }

    public Consulta getConsulta() {
        return consulta;
    }
}
