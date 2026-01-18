package com.paqueteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paqueteria.model.Ruta;
import com.paqueteria.model.Usuario;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Integer> {
    Optional<Ruta> findByUsuarioAndFecha(Usuario usuario, LocalDate fecha);
}
