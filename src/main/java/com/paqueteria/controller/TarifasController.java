package com.paqueteria.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class TarifasController {

    private static final List<Map<String, Object>> TARIFAS_DISTANCIA = List.of(
            Map.of("tipo", "Ciudad", "precio", 50.0f),
            Map.of("tipo", "Provincial", "precio", 100.0f),
            Map.of("tipo", "Nacional", "precio", 200.0f),
            Map.of("tipo", "Internacional", "precio", 500.0f));

    private static final List<Map<String, Object>> TARIFAS_RANGO_PESO = List.of(
            Map.of("min", 0, "max", 10, "precio", 10.0f, "descripcion", "menor de 10kg"),
            Map.of("min", 10, "max", 20, "precio", 20.0f, "descripcion", "10kg - 20kg"),
            Map.of("min", 20, "max", 40, "precio", 35.0f, "descripcion", "20kg - 40kg"),
            Map.of("min", 40, "max", Integer.MAX_VALUE, "precio", 50.0f, "descripcion", "mayor de 40kg"));

    @GetMapping("/tarifas/general")
    public Map<String, Object> getTarifasGeneral(@RequestParam(required = false) String tipo) {

        if (tipo == null) {
            return Map.of("distancia", TARIFAS_DISTANCIA, "rangoPeso", TARIFAS_RANGO_PESO);
        }

        switch (tipo.toLowerCase()) {
            case "distancia":
                return Map.of("distancia", TARIFAS_DISTANCIA);
            case "peso":
                return Map.of("rangoPeso", TARIFAS_RANGO_PESO);
            default:
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Tipo de tarifa no válido: '" + tipo + "'. Tipos válidos: distancia, peso");
        }
    }
}
