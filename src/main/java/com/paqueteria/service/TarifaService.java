package com.paqueteria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paqueteria.dto.TarifaDistanciaDTO;
import com.paqueteria.dto.TarifaRangoPesoDTO;
import com.paqueteria.model.TarifaDistancia;
import com.paqueteria.model.TarifaRangoPeso;
import com.paqueteria.repository.TarifaDistanciaRepository;
import com.paqueteria.repository.TarifaRangoPesoRepository;

@Service
public class TarifaService {

    @Autowired
    private TarifaDistanciaRepository tarifaDistanciaRepository;

    @Autowired
    private TarifaRangoPesoRepository tarifaRangoPesoRepository;

    public List<TarifaDistancia> obtenerTodasTarifasDistancia() {
        return tarifaDistanciaRepository.findAll();
    }

    public List<TarifaRangoPeso> obtenerTodasTarifasPeso() {
        return tarifaRangoPesoRepository.findAll();
    }

    public List<TarifaDistanciaDTO> obtenerTarifasDistanciaActivas() {
        return tarifaDistanciaRepository.findAll().stream()
            .filter(TarifaDistancia::getActiva)
            .map(TarifaDistanciaDTO::new)
            .collect(Collectors.toList());
    }

    public List<TarifaRangoPesoDTO> obtenerTarifasPesoActivas() {
        return tarifaRangoPesoRepository.findAll().stream()
            .filter(TarifaRangoPeso::getActiva)
            .map(TarifaRangoPesoDTO::new)
            .collect(Collectors.toList());
    }
}
