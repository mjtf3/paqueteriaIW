package com.paqueteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.paqueteria.model.Usuario;
import com.paqueteria.model.TipoEnum;
import java.util.Optional;
import java.util.List;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String s);
    List<Usuario> findByTipoAndActivaTrue(TipoEnum tipo);
    Page<Usuario> findByTipo(TipoEnum tipo, Pageable pageable);
}
