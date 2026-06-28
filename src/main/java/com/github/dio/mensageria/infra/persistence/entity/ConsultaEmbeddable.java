package com.github.dio.mensageria.infra.persistence.entity;

import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

@Embeddable
public class ConsultaEmbeddable {

    @Column(name = "consulta_nome")
    private String nome;
    private LocalDateTime dataAtendimento;
    private LocalDateTime dataMarcacao;
    @Enumerated(EnumType.STRING)
    private Consulta.Status status;

    public ConsultaEmbeddable(){}

    public ConsultaEmbeddable(String consulta, LocalDateTime dataAtendimento) {
        this.nome = consulta;
        this.dataAtendimento = dataAtendimento;
        this.dataMarcacao = LocalDateTime.now();
        this.status = Consulta.Status.MARCADO;
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

    public Consulta.Status getStatus() {
        return status;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataAtendimento(LocalDateTime dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public void setDataMarcacao(LocalDateTime dataMarcacao) {
        this.dataMarcacao = dataMarcacao;
    }

    public void setStatus(Consulta.Status status) {
        this.status = status;
    }

}
