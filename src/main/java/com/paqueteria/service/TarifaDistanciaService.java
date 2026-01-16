package com.paqueteria.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paqueteria.dto.TarifaDistanciaDTO;
import com.paqueteria.model.TarifaDistancia;
import com.paqueteria.repository.TarifaDistanciaRepository;

@Service
public class TarifaDistanciaService {

    @Autowired
    private TarifaDistanciaRepository tarifaDistanciaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<TarifaDistanciaDTO> obtenerTodasLasTarifas() {
        List<TarifaDistancia> tarifas = tarifaDistanciaRepository.findAll();

        List<TarifaDistanciaDTO> tarifasDTO = tarifas.stream()
                .map(tarifa -> modelMapper.map(tarifa, TarifaDistanciaDTO.class))
                .toList();

        return tarifasDTO;
    }
}
