package com.paqueteria.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paqueteria.model.TarifaDistancia;
import com.paqueteria.model.TarifaPeso;
import com.paqueteria.repository.TarifaDistanciaRepository;
import com.paqueteria.repository.TarifaPesoRepository;

@Service
public class TarifaService {

    @Autowired
    private TarifaDistanciaRepository tarifaDistanciaRepository;

    @Autowired
    private TarifaPesoRepository tarifaPesoRepository;

    public List<TarifaDistancia> obtenerTodasTarifasDistancia() {
        return tarifaDistanciaRepository.findAll();
    }

    public List<TarifaPeso> obtenerTodasTarifasPeso() {
        return tarifaPesoRepository.findAll();
    }
}
