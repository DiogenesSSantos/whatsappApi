package com.github.dio.mensageria.infra.persistence;

import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.infra.persistence.entity.PacienteEntity;
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

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM tb_paciente WHERE codigo = :codigo", nativeQuery = true)
    int deleteByCodigo(@Param("codigo") String codigo);

    @Transactional
    @Modifying
    @Query(value = "UPDATE tb_paciente SET status = :status WHERE codigo = :codigo", nativeQuery = true)
    int atualizarStatusByCodigo(@Param("codigo") String codigo, @Param("status") String status);

}
