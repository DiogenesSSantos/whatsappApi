package com.github.dio.mensageria.domain.paciente;

import com.github.dio.mensageria.domain.consulta.Consulta;
import com.github.dio.mensageria.domain.contato.Contato;

import java.util.UUID;

public class Paciente {

    private String codigo;
    private String nome;
    private Contato contato;
    private Consulta consulta;

    private Paciente() {
    }


    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public Contato getContato() {
        return contato;
    }

    public Consulta getConsulta() {
        return consulta;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String codigo;
        private String nome;
        private Contato contato;
        private Consulta consulta;

        private Builder() {
        }


        public Builder codigo(String codigo) {
            this.codigo = codigo;
            return this;
        }

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder contato(Contato contato) {
            this.contato = contato;
            return this;
        }

        public Builder consulta(Consulta consulta) {
            this.consulta = consulta;
            return this;
        }

        public Paciente build() {
            if (nome == null || nome.isBlank()) {
                throw new PacienteBuilderException("Nome é obrigatório");
            }
            if (contato == null) {
                throw new PacienteBuilderException("Contato é obrigatório");
            }
            if (consulta == null) {
                throw new PacienteBuilderException("Consulta é obrigatória");
            }

            Paciente paciente = new Paciente();
            paciente.codigo = this.codigo != null ? this.codigo : gerarCodigo() ;
            paciente.nome = nome;
            paciente.contato = contato;
            paciente.consulta = consulta;
            return paciente;
        }

        private String gerarCodigo() {
            return UUID.randomUUID().toString();
        }
    }

}
