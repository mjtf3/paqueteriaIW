package com.paqueteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paqueteria.model.Usuario;
import com.paqueteria.model.TipoEnum;
import java.util.Optional;
import java.util.List;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String s);
    List<Usuario> findByTipoAndActivaTrue(TipoEnum tipo);
    List<Usuario> findByTipo(TipoEnum tipo);
}
