package com.github.dio.mensageria.infra.gateways;

import com.github.dio.mensageria.application.gateways.output.Mensageria;
import com.github.dio.mensageria.application.gateways.output.OllamaGateway;
import com.github.dio.mensageria.domain.mensagem.ResultadoEnvio;
import com.github.dio.mensageria.domain.paciente.Paciente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MensageriaN8N implements Mensageria {
    private static final Logger log = LoggerFactory.getLogger(MensageriaN8N.class);
    private final EvolutionGoClient client;
    private final OllamaGateway ollamaGateway;

    public MensageriaN8N(EvolutionGoClient client, OllamaGateway ollamaGateway) {
        this.client = client;
        this.ollamaGateway = ollamaGateway;
    }


    @Override
    public ResultadoEnvio enviar(Paciente paciente) throws Exception {
        try {
            String nomePaciente = paciente.getNome();
            String nomeConsulta = paciente.getConsulta().getNome();
            String mensagem = ollamaGateway.gerarVariacaoMensagem(nomePaciente, nomeConsulta, paciente.getConsulta().getDataAtendimento());

            var numeros = paciente.getContato().getNumerosCelular();
            for (int i = 0; i < numeros.size(); i++) {
                if (i > 0) {
                    Thread.sleep(60000);
                }
                client.enviarTexto(numeros.get(i).getCelular(), mensagem);
            }

        } catch (Exception e) {
            return new ResultadoEnvio.Falha("Algum motivo no fluxo do envio de mensagem");
        }
        return new ResultadoEnvio.Sucesso();
    }
}
