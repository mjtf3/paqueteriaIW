package com.paqueteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paqueteria.model.TarifaDistancia;

@Repository
public interface TarifaDistanciaRepository extends JpaRepository<TarifaDistancia, Long> {
}

