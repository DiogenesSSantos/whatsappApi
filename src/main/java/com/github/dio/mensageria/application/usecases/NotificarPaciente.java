package com.github.dio.mensageria.application.usecases;

import com.github.dio.mensageria.application.gateways.output.Mensageria;
import com.github.dio.mensageria.application.services.FilaMensagem;
import com.github.dio.mensageria.domain.paciente.Paciente;

import java.util.List;

public class NotificarPaciente {
    private final Mensageria mensageria;
    private final FilaMensagem filaMensagem;

    public NotificarPaciente(Mensageria mensageria, FilaMensagem filaMensagem) {
        this.mensageria = mensageria;
        this.filaMensagem = filaMensagem;
    }

    public void enviar(Paciente paciente) throws Exception {
        mensageria.enviar(paciente);
    }

    public void enfileirar(Paciente paciente) {
        filaMensagem.enfileirar(paciente);
    }

    public void enfileirar(List<Paciente> pacientes) {
        pacientes.forEach(filaMensagem::enfileirar);
    }

    public int filaTamanho() {
        return filaMensagem.tamanho();
    }
}
