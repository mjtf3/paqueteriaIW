package com.paqueteria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paqueteria.model.TarifaRangoPeso;

@Repository
public interface TarifaRangoPesoRepository extends JpaRepository<TarifaRangoPeso, Integer> {

    List<TarifaRangoPeso> findByActiva(Boolean activa);

    @Query("SELECT t FROM TarifaRangoPeso t WHERE :peso >= t.pesoMinimo AND :peso < t.pesoMaximo AND t.activa = true")
    Optional<TarifaRangoPeso> findByPesoAndActiva(@Param("peso") Integer peso);
}
