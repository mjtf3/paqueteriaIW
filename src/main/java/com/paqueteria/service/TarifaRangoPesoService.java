package com.paqueteria.service;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.paqueteria.dto.TarifaRangoPesoDTO;
import com.paqueteria.model.TarifaRangoPeso;
import com.paqueteria.repository.TarifaRangoPesoRepository;

@Service
public class TarifaRangoPesoService {

    @Autowired
    private TarifaRangoPesoRepository tarifaRangoPesoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<TarifaRangoPesoDTO> obtenerTodasLasTarifas() {
        return tarifaRangoPesoRepository.findByActiva(true)
                .stream()
                .map(tarifa -> modelMapper.map(tarifa, TarifaRangoPesoDTO.class))
                .toList();
    }

    public TarifaRangoPesoDTO obtenerTarifaPorPeso(BigDecimal peso) {
        if (peso.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El peso no puede ser negativo: " + peso);
        }

        TarifaRangoPeso tarifa = tarifaRangoPesoRepository.findByPesoAndActiva(peso.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró una tarifa para el peso: " + peso));

        return modelMapper.map(tarifa, TarifaRangoPesoDTO.class);
    }
}
