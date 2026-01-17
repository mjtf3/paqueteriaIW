package com.paqueteria.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paqueteria.dto.EnvioDTO;
import com.paqueteria.model.Envio;
import com.paqueteria.repository.EnvioRepository;
import com.paqueteria.service.EnvioService;

@Controller
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @Autowired
    private EnvioRepository envioRepository;

    // Endpoint para la vista web (HTML)
    @GetMapping("/seguimiento/tracking")
    public String tracking(@RequestParam(required = false) String code, Model model) {
        if (code == null || code.trim().isEmpty()) {
            return "seguimiento";
        }

        code = code.trim();
        model.addAttribute("code", code);

        Optional<EnvioDTO> envioDTO = envioService.getTrackingInfo(code);

        if (envioDTO.isEmpty()) {
            model.addAttribute("error", "No se encontró ningún envío con el código: " + code);
            return "seguimiento";
        }

        EnvioDTO envio = envioDTO.get();
        model.addAttribute("trackingInfo", envio);
        model.addAttribute("message", "Código: " + envio.getLocalizador() + " — Estado: " + envio.getStatus());

        return "seguimiento";
    }

    // Endpoint para la API REST (JSON)
    @GetMapping("/api/envio")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getEstadoEnvio(@RequestParam String localizador) {
        if (localizador == null || localizador.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "El parámetro 'localizador' es obligatorio"));
        }

        Optional<Envio> envioOpt = envioRepository.findByLocalizador(localizador.trim());

        if (envioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Envio no encontrado"));
        }

        Envio envio = envioOpt.get();
        String estadoString = envioService.mapEstadoToFrontendStatus(envio.getEstado());

        Map<String, String> response = new HashMap<>();
        response.put("estado", estadoString);

        return ResponseEntity.ok(response);
    }
}
