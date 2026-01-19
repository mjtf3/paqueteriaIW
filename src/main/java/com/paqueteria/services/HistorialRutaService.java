package com.paqueteria.services;

import com.paqueteria.model.Ruta;
import com.paqueteria.model.Usuario;
import com.paqueteria.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HistorialRutaService {
    @Autowired
    private RutaRepository rutaRepository;

    public List<Ruta> obtenerHistorialWebmaster() {
        return rutaRepository.findAllByOrderByFechaDesc();
    }

    public List<Ruta> obtenerHistorialRepartidor(Usuario usuario) {
        return rutaRepository.findByUsuarioOrderByFechaDesc(usuario);
    }
}
