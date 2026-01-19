package com.paqueteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paqueteria.model.Ruta;

import com.paqueteria.model.Usuario;
import java.util.List;
@Repository

public interface RutaRepository extends JpaRepository<Ruta, Integer> {
	List<Ruta> findAllByOrderByFechaDesc();
	List<Ruta> findByUsuarioOrderByFechaDesc(Usuario usuario);
}
