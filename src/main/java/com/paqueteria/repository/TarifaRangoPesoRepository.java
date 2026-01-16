package com.paqueteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paqueteria.model.TarifaRangoPeso;

@Repository
public interface TarifaRangoPesoRepository extends JpaRepository<TarifaRangoPeso, Integer> {
    
}
