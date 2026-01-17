package com.paqueteria.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.paqueteria.dto.TarifaDistanciaDTO;
import com.paqueteria.dto.TarifaRangoPesoDTO;
import com.paqueteria.security.RequireApiKey;
import com.paqueteria.services.TarifaDistanciaService;
import com.paqueteria.services.TarifaRangoPesoService;

@RestController
@RequestMapping("/api")
@RegisterReflectionForBinding({TarifaDistanciaDTO.class, TarifaRangoPesoDTO.class})
public class TarifasController {

    @Autowired
    private TarifaDistanciaService tarifaDistanciaService;

    @Autowired
    private TarifaRangoPesoService tarifaRangoPesoService;

    @GetMapping("/tarifas/general")
    public Map<String, Object> getTarifasGeneral(@RequestParam(required = false) String tipo) {

        if (tipo == null) {
            return Map.of("distancia", tarifaDistanciaService.obtenerTodasLasTarifas(), "rangoPeso", tarifaRangoPesoService.obtenerTodasLasTarifas());
        }

        switch (tipo.toLowerCase()) {
            case "distancia":
                return Map.of("distancia", tarifaDistanciaService.obtenerTodasLasTarifas());
            case "peso":
                return Map.of("rangoPeso", tarifaRangoPesoService.obtenerTodasLasTarifas());
            default:
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Tipo de tarifa no válido: '" + tipo + "'. Tipos válidos: distancia, peso");
        }
    }

    @RequireApiKey
    @GetMapping("/tarifas/especifica")
    public Map<String, Object> getTarifaEspecifica(@RequestParam(required = false) Integer distancia, @RequestParam(required = false) BigDecimal peso) {
        if (distancia == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El parámetro 'distancia' es obligatorio.");
        }
        if (peso == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El parámetro 'peso' es obligatorio.");
        }
        return Map.of("precio", tarifaDistanciaService.obtenerTarifaPorNumero(distancia).getCoste()
                .add(tarifaRangoPesoService.obtenerTarifaPorPeso(peso).getCoste()));
    }
}
