package com.paqueteria.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TarifasController {

    @GetMapping("/tarifas/general")
    public Map<String, Object> getTarifasGeneral(@RequestParam(required = false) String tipo) {
        List<Map<String, Object>> distancia = List.of(
                Map.of("tipo", "Ciudad", "precio", 50.0f),
                Map.of("tipo", "Provincial", "precio", 100.0f),
                Map.of("tipo", "Nacional", "precio", 200.0f),
                Map.of("tipo", "Internacional", "precio", 500.0f));

        List<Map<String, Object>> rangoPeso = List.of(
                Map.of("min", 0, "max", 10, "precio", 10.0f, "descripcion", "menor de 10kg"),
                Map.of("min", 10, "max", 20, "precio", 20.0f, "descripcion", "10kg - 20kg"),
                Map.of("min", 20, "max", 40, "precio", 35.0f, "descripcion", "20kg - 40kg"),
                Map.of("min", 40, "max", Integer.MAX_VALUE, "precio", 50.0f, "descripcion", "mayor de 40kg"));

        if (tipo == null) {
            return Map.of("distancia", distancia, "rangoPeso", rangoPeso);
        }

        switch (tipo) {
            case "distancia":
                return Map.of("distancia", distancia);
            case "peso":
                return Map.of("rangoPeso", rangoPeso);
            default:
                return Map.of("error", "Tipo de tarifa no válido");
        }
    }
}
