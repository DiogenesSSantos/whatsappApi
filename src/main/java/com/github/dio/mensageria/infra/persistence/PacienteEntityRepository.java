package com.github.dio.mensageria.infra.persistence;

import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.infra.persistence.entity.PacienteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteEntityRepository extends JpaRepository<PacienteEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"contato.numerosCelular"})
    List<PacienteEntity> findAll();

    @EntityGraph(attributePaths = {"contato.numerosCelular"})
    Optional<PacienteEntity> findByCodigo(String codigo);

    @Query(value = """
            SELECT p FROM PacienteEntity p
            LEFT JOIN FETCH p.contato.numerosCelular
            WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT(:nome, '%')))
            AND (:bairro IS NULL OR LOWER(p.contato.bairro) LIKE LOWER(CONCAT(:bairro, '%')))
            AND (:consultaNome IS NULL OR LOWER(p.consulta.nome) LIKE LOWER(CONCAT(:consultaNome, '%')))
            AND (:status IS NULL OR p.consulta.status = :status)
            """,
            countQuery = """
            SELECT COUNT(p) FROM PacienteEntity p
            WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT(:nome, '%')))
            AND (:bairro IS NULL OR LOWER(p.contato.bairro) LIKE LOWER(CONCAT(:bairro, '%')))
            AND (:consultaNome IS NULL OR LOWER(p.consulta.nome) LIKE LOWER(CONCAT(:consultaNome, '%')))
            AND (:status IS NULL OR p.consulta.status = :status)
            """)
    Page<PacienteEntity> buscarComFiltros(
            @Param("nome") String nome,
            @Param("bairro") String bairro,
            @Param("consultaNome") String consultaNome,
            @Param("status") Consulta.Status status,
            Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM tb_paciente WHERE codigo = :codigo", nativeQuery = true)
    int deleteByCodigo(@Param("codigo") String codigo);

    @Transactional
    @Modifying
    @Query(value = "UPDATE tb_paciente SET status = :status WHERE codigo = :codigo", nativeQuery = true)
    int atualizarStatusByCodigo(@Param("codigo") String codigo, @Param("status") String status);

}
