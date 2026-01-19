package com.paqueteria.services;

import com.paqueteria.model.Ruta;
import com.paqueteria.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RutaService {
    @Autowired
    private RutaRepository rutaRepository;

    public Ruta guardarRuta(Ruta ruta) {
        return rutaRepository.save(ruta);
    }
}
