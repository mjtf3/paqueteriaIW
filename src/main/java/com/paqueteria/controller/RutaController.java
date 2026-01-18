package com.paqueteria.controller;

import com.paqueteria.dto.EnvioDTO;
import com.paqueteria.services.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


@Controller
@RequestMapping("/ruta")
public class RutaController {
                
            
    @Autowired
    private EnvioService envioService;

    @GetMapping
    public String mostrarRuta(@RequestParam("repartidorId") Long repartidorId, Model model) {
        List<EnvioDTO> envios = envioService.obtenerEnviosPorRepartidor(repartidorId);
        model.addAttribute("envios", envios);
        model.addAttribute("repartidorId", repartidorId);
        return "ruta";
    }

    @GetMapping("/finalizar")
    public String mostrarResumenGet(@RequestParam("repartidorId") Long repartidorId, Model model) {
        List<EnvioDTO> envios = envioService.obtenerEnviosPorRepartidorTodosEstados(repartidorId);
        int totalEntregados = 0;
        int totalRechazados = 0;
        int totalAusentes = 0;
        int totalNoEntregados = 0;
        int totalPaquetes = envios.size();
        for (EnvioDTO envio : envios) {
            switch (envio.getEstado()) {
                case ENTREGADO:
                    totalEntregados++;
                    break;
                case RECHAZADO:
                    totalRechazados++;
                    break;
                case AUSENTE:
                    totalAusentes++;
                    break;
                case PENDIENTE:
                case RUTA:
                    totalNoEntregados++;
                    break;
                default:
                    break;
            }
        }
        model.addAttribute("totalEntregados", totalEntregados);
        model.addAttribute("totalRechazados", totalRechazados);
        model.addAttribute("totalAusentes", totalAusentes);
        model.addAttribute("totalNoEntregados", totalNoEntregados);
        model.addAttribute("totalPaquetes", totalPaquetes);
        return "resumenRuta";
    }
}
