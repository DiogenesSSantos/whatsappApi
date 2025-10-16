package com.github.dio.mensageria.model;

public class PacienteNaoRespondido {

    private Long pacienteId;
    private String numero;

    public PacienteNaoRespondido(Long pacienteId, String numero) {
        this.pacienteId = pacienteId;
        this.numero = numero;
    }





    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }


    @Override
    public String toString() {
        return "Pendencia{" +
                "pacienteId=" + pacienteId +
                ", numero='" + numero + '\'' +
                '}';
    }
}

