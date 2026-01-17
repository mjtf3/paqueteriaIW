package com.paqueteria.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import com.paqueteria.dto.CrearEnvioDTO;
import com.paqueteria.dto.EnvioDTO;
import com.paqueteria.model.DistanciaEnum;
import com.paqueteria.model.Envio;
import com.paqueteria.model.EstadoEnum;
import com.paqueteria.repository.EnvioRepository;
import com.paqueteria.security.RequireApiKey;
import com.paqueteria.services.EnvioService;

import jakarta.validation.Valid;

@Controller
@RegisterReflectionForBinding({CrearEnvioDTO.class, EnvioDTO.class, EstadoEnum.class, DistanciaEnum.class})
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @Autowired
    private EnvioRepository envioRepository;

    // Endpoint para la vista web (HTML)
    @GetMapping("/seguimiento/tracking")
    public String tracking(@RequestParam(required = false) String code, Model model) {
        if (code == null || code.trim().isEmpty()) {
            setTrackingDefaults(model);
            return "seguimiento";
        }

        code = code.trim();
        model.addAttribute("code", code);

        Optional<EnvioDTO> envioDTO = envioService.getTrackingInfo(code);

        if (envioDTO.isEmpty()) {
            setTrackingDefaults(model);
            model.addAttribute("error", "No se encontró ningún envío con el código: " + code);
            return "seguimiento";
        }

        EnvioDTO envio = envioDTO.get();
        model.addAttribute("trackingInfo", envio);
        model.addAttribute("hasTracking", true);
        model.addAttribute("message", "Código: " + envio.getLocalizador() + " — Estado: " + envio.getEstadoString());

        // UI Logic pre-calculation
        model.addAttribute("stage1Active", envio.isAlmacenOPosterior());
        model.addAttribute("stage2Active", envio.isRepartoOPosterior());
        model.addAttribute("stage3Active", envio.isFinalizado());
        
        model.addAttribute("arrow1Active", envio.isRepartoOPosterior());
        model.addAttribute("arrow2Active", envio.isFinalizado());

        model.addAttribute("showEntregadoIcon", envio.isEntregadoExitoso());
        model.addAttribute("showAusenteIcon", envio.isAusente());
        model.addAttribute("showRechazadoIcon", envio.isRechazado());
        
        model.addAttribute("showInfo", true);

        return "seguimiento";
    }

    private void setTrackingDefaults(Model model) {
        model.addAttribute("hasTracking", false);
        model.addAttribute("stage1Active", false);
        model.addAttribute("stage2Active", false);
        model.addAttribute("stage3Active", false);
        model.addAttribute("arrow1Active", false);
        model.addAttribute("arrow2Active", false);
        model.addAttribute("showEntregadoIcon", true); // Default icon shown if no specific state
        model.addAttribute("showAusenteIcon", false);
        model.addAttribute("showRechazadoIcon", false);
        model.addAttribute("showInfo", false);
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

    @RequireApiKey
    @PostMapping("/api/envios")
    @ResponseStatus(HttpStatus.CREATED)
    public EnvioDTO crearEnvio(@Valid @RequestBody(required = false) CrearEnvioDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body no puede estar vacío");
        }
        // Cuando tengamos usuarios re tiene que pillas la api key, un servicio get id by api key y pasarla aqui
        return envioService.crearEnvio(dto, 1); // TODO: Usuario simulado con ID 1; obtener ID de usuario autenticado
    }
}
