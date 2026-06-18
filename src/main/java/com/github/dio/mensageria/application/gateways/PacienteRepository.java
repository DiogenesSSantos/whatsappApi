package com.github.dio.mensageria.application.gateways;


import com.github.dio.mensageria.domain.entities.paciente.Paciente;

public interface PacienteRepository {

    Paciente salvar(Paciente paciente);

    Paciente salvarNaoPossuiWhatsapp(Paciente paciente);

}
