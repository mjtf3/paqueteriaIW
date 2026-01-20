package com.paqueteria.services;

import com.paqueteria.model.Ruta;
import com.paqueteria.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.paqueteria.model.Usuario;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class RutaService {
    @Autowired
    private RutaRepository rutaRepository;

    public Ruta guardarRuta(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public Optional<Ruta> buscarRutaPorUsuarioYFecha(Usuario usuario, LocalDate fecha) {
        // Repositorio no tiene método específico, usar findByUsuarioOrderByFechaDesc y filtrar
        return rutaRepository.findByUsuarioOrderByFechaDesc(usuario).stream()
                .filter(r -> r.getFecha() != null && r.getFecha().equals(fecha))
                .findFirst();
    }
}
