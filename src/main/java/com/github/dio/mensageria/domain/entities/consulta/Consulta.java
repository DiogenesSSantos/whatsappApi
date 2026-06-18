package com.github.dio.mensageria.domain.entities.consulta;

import java.time.LocalDateTime;

public class Consulta {

    private String nome;
    private LocalDateTime dataAtendimento;
    private LocalDateTime dataMarcacao;
    private Status status;


    public Consulta(String consulta, LocalDateTime dataAtendimento) {
        validarDataConsulta(dataAtendimento);
        this.nome = consulta;
        this.dataAtendimento = dataAtendimento;
        this.dataMarcacao = LocalDateTime.now();
        this.status = Status.MARCADO;
    }

    private void validarDataConsulta(LocalDateTime dataConsulta) {
        if (dataConsulta.isBefore(LocalDateTime.now())) {
            throw new DataPassadoException("Data consulta não pode ser no passado");
        }
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

    public enum Status {
        MARCADO,
        AGUARDANDO,
        NAO_POSSUI_WHATSAPP,
        REJEITADO
    }
}
