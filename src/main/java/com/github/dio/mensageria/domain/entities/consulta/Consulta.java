package com.github.dio.mensageria.domain.entities.consulta;

import com.github.dio.mensageria.domain.exception.DataPassadoException;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class Consulta {

    private String nome;
    private LocalDateTime dataConsulta;
    private LocalDateTime dataMarcacao;
    private Status status;


    public Consulta(String consulta, LocalDateTime dataConsulta) {
        validarDataConsulta(dataConsulta);
        this.nome = consulta;
        this.dataConsulta = dataConsulta;
        this.dataMarcacao = LocalDateTime.now();
        this.status = Status.MARCADO;
    }


    // Capturar a exception com handler, DateTimeParseExceptio e a personalizado (IllegalArgumentException)
    private void validarDataConsulta(LocalDateTime dataConsulta) {
        if (dataConsulta.isBefore(LocalDateTime.now())) {
            throw new DataPassadoException("Data consulta não pode ser no passado");
        }
    }



    public enum Status {
        MARCADO,
        REJEITADO
    }
}
