package com.github.dio.mensageria.application.gateways.output;

import com.github.dio.mensageria.domain.mensagem.ResultadoEnvio;
import com.github.dio.mensageria.domain.paciente.Paciente;

public interface Mensageria {
    ResultadoEnvio enviar(Paciente paciente) throws Exception;

}
