package com.paqueteria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paqueteria.model.Envio;
import com.paqueteria.model.EstadoEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Integer> {
    Page<Envio> findByEstado(EstadoEnum estado, Pageable pageable);

    // Calcular el peso total de envíos asignados a un repartidor en una fecha específica
    @Query("SELECT COALESCE(SUM(e.peso), 0) FROM Envio e JOIN e.ruta r WHERE r.usuario.id = :repartidorId AND r.fecha = :fecha")
    BigDecimal calcularPesoAsignadoPorRepartidorYFecha(
        @Param("repartidorId") Integer repartidorId,
        @Param("fecha") LocalDate fecha
    );

    // Verificar si existen envíos urgentes (más de 5 días) en estados pendientes
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Envio e WHERE e.fecha < :fechaLimite AND e.estado IN ('PENDIENTE', 'AUSENTE', 'RECHAZADO')")
    boolean existeEnviosUrgentes(@Param("fechaLimite") LocalDate fechaLimite);
    Optional<Envio> findByLocalizador(String localizador);
}
