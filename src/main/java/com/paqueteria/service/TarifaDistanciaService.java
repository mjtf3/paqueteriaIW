package com.paqueteria.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.paqueteria.dto.TarifaDistanciaDTO;
import com.paqueteria.model.DistanciaEnum;
import com.paqueteria.model.TarifaDistancia;
import com.paqueteria.repository.TarifaDistanciaRepository;

@Service
public class TarifaDistanciaService {

    @Autowired
    private TarifaDistanciaRepository tarifaDistanciaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<TarifaDistanciaDTO> obtenerTodasLasTarifas() {
        return tarifaDistanciaRepository.findByActiva(true)
                .stream()
                .map(tarifa -> modelMapper.map(tarifa, TarifaDistanciaDTO.class))
                .toList();
    }

    public TarifaDistanciaDTO obtenerTarifaPorNumero(Integer numero) {
        int maxDistancias = DistanciaEnum.values().length;
        if (numero < 1 || numero > maxDistancias) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de la distancia debe estar entre 1 y " + maxDistancias);
        }

        DistanciaEnum distancia = DistanciaEnum.values()[numero - 1];

        TarifaDistancia tarifa = tarifaDistanciaRepository.findByDistanciaAndActiva(distancia, true)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró una tarifa para la distancia: " + distancia));

        return modelMapper.map(tarifa, TarifaDistanciaDTO.class);
    }
}
