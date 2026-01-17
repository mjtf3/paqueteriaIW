package com.paqueteria.controller;

import com.paqueteria.service.TarifaDistanciaService;
import com.paqueteria.service.TarifaRangoPesoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TarifasWebController {

    @Autowired
    private TarifaDistanciaService tarifaDistanciaService;

    @Autowired
    private TarifaRangoPesoService tarifaRangoPesoService;

    @GetMapping("/tarifas")
    public String getTarifas(Model model) {
        model.addAttribute("tarifasDistancia", tarifaDistanciaService.obtenerTarifasDistanciaActivas());
        model.addAttribute("tarifasRangoPeso", tarifaRangoPesoService.obtenerTarifasPesoActivas());
        model.addAttribute("mostrarTodo", true);
        return "tarifas";
    }
}
