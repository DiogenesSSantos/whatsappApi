package com.github.dio.mensageria.application.gateways.input;

import com.github.dio.mensageria.domain.paciente.Paciente;

public interface GerarMensagemAIUseCase {
    String gerar(Paciente paciente) throws Exception;
}
