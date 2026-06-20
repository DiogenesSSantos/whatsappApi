package com.github.dio.mensageria.infra.persistence;

import com.github.dio.mensageria.infra.persistence.entity.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteEntityRepository extends JpaRepository<PacienteEntity, Long> {
}
