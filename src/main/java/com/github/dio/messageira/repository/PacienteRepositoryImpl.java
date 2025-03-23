package com.github.dio.messageira.repository;

import com.github.dio.messageira.infraestruct.assembler.AssembleFiltro;
import com.github.dio.messageira.model.Filtro;
import com.github.dio.messageira.model.Paciente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PacienteRepositoryImpl implements PacienteRepositoryCustomSQL {
    private static final Logger log = LoggerFactory.getLogger(PacienteRepositoryImpl.class);


    @Autowired
    private EntityManager entityManager;


    @Override
    public List<Paciente> filtrar(Filtro filtro) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        var querry = builder.createQuery(Paciente.class);
        var root = querry.from(Paciente.class);


        List<Predicate> predicates = new ArrayList<>();

        if (filtro.getNome() != null) {
            log.info(String.format("Filtrando por nome: %s" , filtro.getNome()));
            predicates.add(builder.like(root.get("nome"), filtro.getNome() + "%"));
        }

        if (filtro.getBairro() != null) {
            log.info(String.format("Filtrando por bairro: %s" , filtro.getBairro()));
            predicates.add(builder.like(root.get("bairro"), filtro.getBairro() + "%"));
        }

        if (filtro.getDataMarcacaoInicial() != null) {
            log.info(String.format("Filtrando por data inicial: %s" , filtro.getDataMarcacaoInicial()));
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataMarcacao"), filtro.getDataMarcacaoInicial()));

        }

        if (filtro.getDataMarcacaoFinal() != null) {
            log.info(String.format("Filtrando por data Final: %s" , filtro.getDataMarcacaoFinal()));
            predicates.add(builder.lessThanOrEqualTo(root.get("dataMarcacao"), filtro.getDataMarcacaoFinal()));

        }

        if (filtro.getTipoConsulta() != null) {
            log.info(String.format("Filtrando por consulta: %s" , filtro.getTipoConsulta()));
            predicates.add(builder.like(root.get("consulta"), filtro.getTipoConsulta() +"%"));
        }

        if (filtro.getMotivo() != null) {
            log.info(String.format("Filtrando por Motivo: %s" , filtro.getMotivo()));
            predicates.add(builder.and(builder.equal(root.get("motivo") , filtro.getMotivo())));
        }

        querry.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Paciente> pacienteTypedQuery = entityManager.createQuery(querry);

        return pacienteTypedQuery.getResultList();
    }
}
