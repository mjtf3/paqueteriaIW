package com.paqueteria.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
