package com.github.dio.mensageria.application.services;

import com.github.dio.mensageria.application.gateways.output.Mensageria;
import com.github.dio.mensageria.domain.mensagem.ResultadoEnvio;
import com.github.dio.mensageria.domain.paciente.Paciente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

public class FilaMensagem {
    private static final Logger log = LoggerFactory.getLogger(FilaMensagem.class);
    private final LinkedBlockingQueue<Paciente> fila = new LinkedBlockingQueue<>();
    private final Mensageria mensageria;
    private final Thread workerThread;

    public FilaMensagem(Mensageria mensageria, long delayEntrePacientesMs) {
        this.mensageria = mensageria;
        this.workerThread = new Thread(() -> processarFila(delayEntrePacientesMs), "fila-mensageria");
        this.workerThread.setDaemon(true);
        this.workerThread.start();
    }

    public void enfileirar(Paciente paciente) {
        fila.offer(paciente);
        log.info("Paciente enfileirado: {}. Tamanho da fila: {}", paciente.getNome(), fila.size());
    }

    public int tamanho() {
        return fila.size();
    }

    private void processarFila(long delayEntrePacientesMs) {
        while (true) {
            try {
                Paciente paciente = fila.take();
                log.info("Processando paciente: {}", paciente.getNome());
                ResultadoEnvio resultado = mensageria.enviar(paciente);

                if (resultado instanceof ResultadoEnvio.Sucesso) {
                    log.info("Paciente processado com sucesso: {}", paciente.getNome());
                } else if (resultado instanceof ResultadoEnvio.Falha falha) {
                    log.warn("Falha ao enviar mensagem para {}: {}", paciente.getNome(), falha.motivo());
                }

                if (!fila.isEmpty() && delayEntrePacientesMs > 0) {
                    log.info("Aguardando {}ms antes do próximo paciente...", delayEntrePacientesMs);
                    Thread.sleep(delayEntrePacientesMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Fila de mensagens interrompida");
                break;
            } catch (Exception e) {
                log.error("Erro ao processar paciente na fila", e);
            }
        }
    }
}
