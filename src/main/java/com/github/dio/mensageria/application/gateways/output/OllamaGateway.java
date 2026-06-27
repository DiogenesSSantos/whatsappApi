package com.github.dio.mensageria.application.gateways.output;

import java.time.LocalDateTime;

public interface OllamaGateway {
    String gerarVariacaoMensagem(String nomePaciente, String nomeConsulta, LocalDateTime dataAtendimento) throws Exception;

}
