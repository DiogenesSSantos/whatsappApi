package com.github.dio.mensageria.infra.gateways;

import com.github.dio.mensageria.application.gateways.output.Mensageria;
import com.github.dio.mensageria.application.gateways.output.OllamaGateway;
import com.github.dio.mensageria.domain.mensagem.ResultadoEnvio;
import com.github.dio.mensageria.domain.paciente.Paciente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MensageriaImplGateway implements Mensageria {
    private static final Logger log = LoggerFactory.getLogger(MensageriaImplGateway.class);
    private final EvolutionGoClient client;
    private final OllamaGateway ollamaGateway;

    public MensageriaImplGateway(EvolutionGoClient client, OllamaGateway ollamaGateway) {
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
            log.error("Erro ao enviar mensagem para {}: {}", paciente.getNome(), e.getMessage(), e);
            return new ResultadoEnvio.Falha(e.getMessage());
        }
        return new ResultadoEnvio.Sucesso();
    }
}
