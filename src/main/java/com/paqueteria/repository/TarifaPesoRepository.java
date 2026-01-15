package com.paqueteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paqueteria.model.TarifaPeso;

@Repository
public interface TarifaPesoRepository extends JpaRepository<TarifaPeso, Long> {
}

