package com.github.dio.mensageria.infra.persistence.entity;


import com.github.dio.mensageria.domain.consulta.Consulta;
import com.github.dio.mensageria.domain.contato.Contato;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_paciente")
public class PacienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 36)
    private String codigo;

    @Column(name = "nome_paciente")
    private String nome;

    @Embedded
    private ContatoEmbeddable contato;

    @Embedded
    private ConsultaEmbeddable consulta;


    public PacienteEntity(){}

    public PacienteEntity(String codigo, String nome, ContatoEmbeddable contato, ConsultaEmbeddable consulta) {
        this.codigo = codigo;
        this.nome = nome;
        this.contato = contato;
        this.consulta = consulta;
    }


    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public ContatoEmbeddable getContato() {
        return contato;
    }

    public ConsultaEmbeddable getConsulta() {
        return consulta;
    }
}
