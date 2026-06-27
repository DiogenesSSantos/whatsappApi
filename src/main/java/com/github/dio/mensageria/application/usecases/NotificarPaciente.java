package com.github.dio.mensageria.application.usecases;

import com.github.dio.mensageria.application.gateways.output.Mensageria;
import com.github.dio.mensageria.domain.paciente.Paciente;

public class NotificarPaciente  {
    private  final Mensageria mensageria;

    public NotificarPaciente(Mensageria mensageria) {
        this.mensageria = mensageria;
    }

    public void enviar(Paciente paciente) throws Exception {
        mensageria.enviar(paciente);
    }
}
