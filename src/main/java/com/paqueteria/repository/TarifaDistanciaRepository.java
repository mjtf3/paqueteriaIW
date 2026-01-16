package com.paqueteria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paqueteria.model.DistanciaEnum;
import com.paqueteria.model.TarifaDistancia;

@Repository
public interface TarifaDistanciaRepository extends JpaRepository<TarifaDistancia, Integer> {

    Optional<TarifaDistancia> findByDistanciaAndActiva(DistanciaEnum distancia);

    List<TarifaDistancia> findByActiva(Boolean activa);
}
