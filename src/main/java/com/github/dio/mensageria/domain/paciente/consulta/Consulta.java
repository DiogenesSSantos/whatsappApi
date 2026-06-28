package com.github.dio.mensageria.domain.paciente.consulta;

import java.time.LocalDateTime;

public class Consulta {

    private String nome;
    private LocalDateTime dataAtendimento;
    private LocalDateTime dataMarcacao;
    private Status status;


    public Consulta() {
    }

    public Consulta(String consulta, LocalDateTime dataAtendimento) {
        validarDataAtendimento(dataAtendimento);
        this.nome = consulta;
        this.dataAtendimento = dataAtendimento;
        this.dataMarcacao = LocalDateTime.now();
        this.status = Status.MARCADO;
    }

    public static Consulta deDadosPersistidos(String nome, LocalDateTime dataAtendimento, LocalDateTime dataMarcacao, Status status) {
        Consulta c = new Consulta();
        c.nome = nome;
        c.dataAtendimento = dataAtendimento;
        c.dataMarcacao = dataMarcacao;
        c.status = status;
        return c;
    }

    public void atualizarNome(String nome) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome da consulta não pode vázio");
        this.nome = nome;
    }

    public void atualizarDataAtendimento(LocalDateTime dataAtendimento) {
        validarDataAtendimento(dataAtendimento);
        this.dataAtendimento = dataAtendimento;
    }

    public void atualizarDataMarcacao(LocalDateTime dataMarcacao) {
        if (dataMarcacao.isAfter(this.dataAtendimento)) {
            throw new IllegalArgumentException(
                    "Data de marcação da consulta não pode ser depois da data de atendimento");
        }
        this.dataMarcacao = dataMarcacao;
    }

    public void atualizarStatus(Status status) {
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public LocalDateTime getDataAtendimento() {
        return dataAtendimento;
    }

    public LocalDateTime getDataMarcacao() {
        return dataMarcacao;
    }

    public Status getStatus() {
        return status;
    }


    @Override
    public String toString() {
        return "Consulta{" +
                "nome='" + nome + '\'' +
                ", dataAtendimento=" + dataAtendimento +
                ", dataMarcacao=" + dataMarcacao +
                ", status=" + status +
                '}';
    }


    private void validarDataAtendimento(LocalDateTime dataAtendimento) {
        if (dataAtendimento.isBefore(LocalDateTime.now())) {
            throw new DataPassadoException("Data consulta não pode ser no passado");
        }
    }

    public enum Status {
        MARCADO,
        AGUARDANDO,
        NAO_POSSUI_WHATSAPP,
        REJEITADO;
    }
}
