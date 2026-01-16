package com.paqueteria.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.paqueteria.model.TarifaDistancia;
import com.paqueteria.model.TarifaRangoPeso;
import com.paqueteria.service.TarifaService;

@RestController
@RequestMapping("/api")
public class TarifasController {

    @Autowired
    private TarifaService tarifaService;

    @GetMapping("/tarifas/general")
    public Map<String, Object> getTarifasGeneral(@RequestParam(required = false) String tipo) {

        if (tipo == null) {
            return Map.of(
                "distancia", convertirTarifasDistancia(tarifaService.obtenerTodasTarifasDistancia()),
                "rangoPeso", convertirTarifasPeso(tarifaService.obtenerTodasTarifasPeso())
            );
        }

        switch (tipo.toLowerCase()) {
            case "distancia":
                return Map.of("distancia", convertirTarifasDistancia(tarifaService.obtenerTodasTarifasDistancia()));
            case "peso":
                return Map.of("rangoPeso", convertirTarifasPeso(tarifaService.obtenerTodasTarifasPeso()));
            default:
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Tipo de tarifa no válido: '" + tipo + "'. Tipos válidos: distancia, peso");
        }
    }

    private List<Map<String, Object>> convertirTarifasDistancia(List<TarifaDistancia> tarifas) {
        return tarifas.stream()
            .filter(TarifaDistancia::getActiva)
            .map(t -> {
                Map<String, Object> map = new HashMap<>();
                map.put("distancia", t.getDistancia());
                map.put("coste", t.getCoste());
                return map;
            })
            .collect(Collectors.toList());
    }

    private List<Map<String, Object>> convertirTarifasPeso(List<TarifaRangoPeso> tarifas) {
        return tarifas.stream()
            .filter(TarifaRangoPeso::getActiva)
            .map(t -> {
                Map<String, Object> map = new HashMap<>();
                map.put("pesoMinimo", t.getPesoMinimo());
                map.put("pesoMaximo", t.getPesoMaximo());
                map.put("coste", t.getCoste());
                return map;
            })
            .collect(Collectors.toList());
    }
}
