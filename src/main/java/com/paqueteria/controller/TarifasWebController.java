package com.paqueteria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paqueteria.service.TarifaService;

@Controller
public class TarifasWebController {

    @Autowired
    private TarifaService tarifaService;

    @GetMapping("/tarifas")
    public String getTarifas(@RequestParam(required = false) String tipo, Model model) {

        if (tipo == null || tipo.isEmpty()) {
            model.addAttribute("tarifasDistancia", tarifaService.obtenerTarifasDistanciaActivas());
            model.addAttribute("tarifasRangoPeso", tarifaService.obtenerTarifasPesoActivas());
            model.addAttribute("mostrarTodo", true);
        } else {
            switch (tipo.toLowerCase()) {
                case "distancia":
                    model.addAttribute("tarifasDistancia", tarifaService.obtenerTarifasDistanciaActivas());
                    model.addAttribute("mostrarDistancia", true);
                    break;
                case "peso":
                    model.addAttribute("tarifasRangoPeso", tarifaService.obtenerTarifasPesoActivas());
                    model.addAttribute("mostrarPeso", true);
                    break;
                default:
                    model.addAttribute("error", "Tipo de tarifa no válido: '" + tipo + "'. Tipos válidos: distancia, peso");
            }
        }

        return "tarifas";
    }
}
