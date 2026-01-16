package com.paqueteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paqueteria.model.Ruta;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Integer> {
    
}
