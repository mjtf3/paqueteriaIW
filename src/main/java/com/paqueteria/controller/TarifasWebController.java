package com.paqueteria.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paqueteria.model.TarifaDistancia;
import com.paqueteria.model.TarifaPeso;
import com.paqueteria.service.TarifaService;

@Controller
public class TarifasWebController {

    @Autowired
    private TarifaService tarifaService;

    @GetMapping("/tarifas")
    public String getTarifas(@RequestParam(required = false) String tipo, Model model) {

        model.addAttribute("currentPage", "tarifas");

        if (tipo == null || tipo.isEmpty()) {
            model.addAttribute("tarifasDistancia", convertirTarifasDistancia(tarifaService.obtenerTodasTarifasDistancia()));
            model.addAttribute("tarifasRangoPeso", convertirTarifasPeso(tarifaService.obtenerTodasTarifasPeso()));
            model.addAttribute("mostrarTodo", true);
        } else {
            switch (tipo.toLowerCase()) {
                case "distancia":
                    model.addAttribute("tarifasDistancia", convertirTarifasDistancia(tarifaService.obtenerTodasTarifasDistancia()));
                    model.addAttribute("mostrarDistancia", true);
                    break;
                case "peso":
                    model.addAttribute("tarifasRangoPeso", convertirTarifasPeso(tarifaService.obtenerTodasTarifasPeso()));
                    model.addAttribute("mostrarPeso", true);
                    break;
                default:
                    model.addAttribute("error", "Tipo de tarifa no válido: '" + tipo + "'. Tipos válidos: distancia, peso");
            }
        }

        return "tarifas";
    }

    private List<Map<String, Object>> convertirTarifasDistancia(List<TarifaDistancia> tarifas) {
        return tarifas.stream()
            .map(t -> {
                Map<String, Object> map = new HashMap<>();
                map.put("tipo", t.getTipo());
                map.put("precio", t.getPrecio());
                return map;
            })
            .collect(Collectors.toList());
    }

    private List<Map<String, Object>> convertirTarifasPeso(List<TarifaPeso> tarifas) {
        return tarifas.stream()
            .map(t -> {
                Map<String, Object> map = new HashMap<>();
                map.put("min", t.getPesoMin());
                map.put("max", t.getPesoMax());
                map.put("precio", t.getPrecio());
                map.put("descripcion", t.getDescripcion());
                return map;
            })
            .collect(Collectors.toList());
    }
}
