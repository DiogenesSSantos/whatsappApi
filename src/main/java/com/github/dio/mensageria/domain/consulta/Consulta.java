package com.github.dio.mensageria.domain.consulta;

import java.time.LocalDateTime;

public class Consulta {

    private String nome;
    private LocalDateTime dataAtendimento;
    private LocalDateTime dataMarcacao;
    private Status status;


    public Consulta() {
    }

    public Consulta(String consulta, LocalDateTime dataAtendimento) {
        validarDataConsulta(dataAtendimento);
        this.nome = consulta;
        this.dataAtendimento = dataAtendimento;
        this.dataMarcacao = LocalDateTime.now();
        this.status = Status.MARCADO;
    }

    private void validarDataConsulta(LocalDateTime dataAtendimento) {
        if (dataAtendimento.isBefore(LocalDateTime.now())) {
            throw new DataPassadoException("Data consulta não pode ser no passado");
        }
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

    public void setStatus(Status status) {
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

    public enum Status {
        MARCADO,
        AGUARDANDO,
        NAO_POSSUI_WHATSAPP,
        REJEITADO
    }
}
