package com.paqueteria.controller;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.paqueteria.dto.TarifaDistanciaDTO;
import com.paqueteria.dto.TarifaRangoPesoDTO;
import com.paqueteria.service.TarifaDistanciaService;
import com.paqueteria.service.TarifaRangoPesoService;

@Controller
@RegisterReflectionForBinding({TarifaDistanciaDTO.class, TarifaRangoPesoDTO.class})
public class TarifasWebController {

    @Autowired
    private TarifaDistanciaService tarifaDistanciaService;

    @Autowired
    private TarifaRangoPesoService tarifaRangoPesoService;

    @GetMapping("/tarifas")
    public String getTarifas(Model model) {
        model.addAttribute("tarifasDistancia", tarifaDistanciaService.obtenerTarifasDistanciaActivas());
        model.addAttribute("tarifasRangoPeso", tarifaRangoPesoService.obtenerTarifasPesoActivas());
        return "tarifas";
    }
}
