package com.paqueteria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paqueteria.model.Envio;
import com.paqueteria.model.EstadoEnum;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Integer> {
    Page<Envio> findByEstado(EstadoEnum estado, Pageable pageable);
}
