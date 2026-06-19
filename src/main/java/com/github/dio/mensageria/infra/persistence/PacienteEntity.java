package com.github.dio.mensageria.infra.persistence;


import com.github.dio.mensageria.domain.consulta.Consulta;
import com.github.dio.mensageria.domain.contato.Contato;
import jakarta.persistence.*;

//@Entity
//@Table(
//        name = "tb_paciente"
//)
public class PacienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 36)
    private String codigo;
    private String nome;
    private Contato contato;
    private Consulta consulta;


    public PacienteEntity(){}

    public PacienteEntity(String codigo, String nome, Contato contato, Consulta consulta) {
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

    public Contato getContato() {
        return contato;
    }

    public Consulta getConsulta() {
        return consulta;
    }
}
