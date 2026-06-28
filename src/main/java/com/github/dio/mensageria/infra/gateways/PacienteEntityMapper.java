package com.github.dio.mensageria.infra.gateways;

import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.domain.paciente.contato.Contato;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.infra.persistence.entity.ConsultaEmbeddable;
import com.github.dio.mensageria.infra.persistence.entity.ContatoEmbeddable;
import com.github.dio.mensageria.infra.persistence.entity.NumeroEmbeddable;
import com.github.dio.mensageria.infra.persistence.entity.PacienteEntity;

import java.util.ArrayList;
import java.util.List;

public class PacienteEntityMapper {


    public PacienteEntity modelToEntity(Paciente paciente) {
        return new PacienteEntity(paciente.getCodigo(),
                paciente.getNome(),
                contatoToContatoEntity(paciente.getContato()),
                consultaToConsultaEmbeddable(paciente.getConsulta()));
    }

    public Paciente entityToModel(PacienteEntity pacienteEntity) {
        return Paciente.builder()
                .codigo(pacienteEntity.getCodigo())
                .nome(pacienteEntity.getNome())
                .consulta(consultaEntityToConsultaModel(pacienteEntity.getConsulta()))
                .contato(contatoToContatoEntity(pacienteEntity.getContato()))
                .build();

    }


    public PacienteEntity atualizarEntity(PacienteEntity entity, Paciente paciente) {
        entity.setNome(paciente.getNome());

        ContatoEmbeddable contato = entity.getContato();
        contato.setBairro(paciente.getContato().getBairro());

        List<NumeroEmbeddable> novosNumeros = paciente.getContato().getNumerosCelular().stream()
                .map(n -> new NumeroEmbeddable(n.getCelular(), n.isWhatsapp()))
                .toList();

        boolean numerosMudaram = !mesmosNumeros(contato.getNumerosCelular(), novosNumeros);
        if (numerosMudaram) {
            contato.getNumerosCelular().clear();
            contato.getNumerosCelular().addAll(novosNumeros);
        }

        ConsultaEmbeddable consulta = entity.getConsulta();
        consulta.setNome(paciente.getConsulta().getNome());
        consulta.setDataAtendimento(paciente.getConsulta().getDataAtendimento());
        consulta.setDataMarcacao(paciente.getConsulta().getDataMarcacao());
        consulta.setStatus(paciente.getConsulta().getStatus());

        return entity;
    }

    private boolean mesmosNumeros(List<NumeroEmbeddable> atual, List<NumeroEmbeddable> novo) {
        if (atual.size() != novo.size()) return false;
        for (int i = 0; i < atual.size(); i++) {
            NumeroEmbeddable a = atual.get(i);
            NumeroEmbeddable n = novo.get(i);
            if (!a.getCelular().equals(n.getCelular()) || a.isWhatsapp() != n.isWhatsapp()) {
                return false;
            }
        }
        return true;
    }


    private Consulta consultaEntityToConsultaModel(ConsultaEmbeddable consultaEmbeddable) {
        return Consulta.deDadosPersistidos(
                consultaEmbeddable.getNome(),
                consultaEmbeddable.getDataAtendimento(),
                consultaEmbeddable.getDataMarcacao(),
                consultaEmbeddable.getStatus()
        );
    }

    private ConsultaEmbeddable consultaToConsultaEmbeddable(Consulta consulta) {
        ConsultaEmbeddable consultaEmbeddable = new ConsultaEmbeddable();
        consultaEmbeddable.setNome(consulta.getNome());
        consultaEmbeddable.setDataAtendimento(consulta.getDataAtendimento());
        consultaEmbeddable.setDataMarcacao(consulta.getDataMarcacao());
        consultaEmbeddable.setStatus(consulta.getStatus());

        return consultaEmbeddable;
    }


    private Contato contatoToContatoEntity(ContatoEmbeddable contatoEmbeddable) {
        var contato = new Contato(new ArrayList<>(), contatoEmbeddable.getBairro());
        contatoEmbeddable.getNumerosCelular().forEach(numeroEmbeddable ->
                contato.adicionarNumeroCelular(numeroEmbeddable.getCelular()));

        return contato;
    }

    private ContatoEmbeddable contatoToContatoEntity(Contato contato) {
        ContatoEmbeddable contatoEmbeddable = new ContatoEmbeddable(new ArrayList<>(), contato.getBairro());
        contato.getNumerosCelular().forEach(numero ->
                contatoEmbeddable.getNumerosCelular().add(new NumeroEmbeddable(numero.getCelular(),
                        numero.isWhatsapp())));


        return contatoEmbeddable;
    }

}
