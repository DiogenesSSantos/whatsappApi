package com.github.dio.mensageria.application.usecases;

import com.github.dio.mensageria.application.gateways.input.GerarMensagemAIUseCase;
import com.github.dio.mensageria.application.gateways.output.OllamaGateway;
import com.github.dio.mensageria.domain.paciente.Paciente;

public class GeradorDeMensagemAI implements GerarMensagemAIUseCase {

    private final OllamaGateway ollamaGateway;

    public GeradorDeMensagemAI(OllamaGateway ollamaGateway) {
        this.ollamaGateway = ollamaGateway;
    }

    @Override
    public String gerar(Paciente paciente) throws Exception {
        return ollamaGateway.gerarVariacaoMensagem(paciente.getNome(),
                paciente.getConsulta().getNome(),
                paciente.getConsulta().getDataAtendimento());
    }
}
