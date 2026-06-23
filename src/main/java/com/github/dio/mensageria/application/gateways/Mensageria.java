package com.github.dio.mensageria.application.gateways;

import com.github.dio.mensageria.application.usecases.ResultadoEnvio;
import com.github.dio.mensageria.domain.paciente.Paciente;

public interface Mensageria {
    ResultadoEnvio enviar(Paciente paciente);

}
