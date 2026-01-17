package com.paqueteria.controller;

import com.paqueteria.dto.CrearEnvioDTO;
import com.paqueteria.dto.EnvioDTO;
import com.paqueteria.security.RequireApiKey;
import com.paqueteria.services.EnvioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/envios")
public class EnviosController {

    @Autowired
    private EnvioService envioService;

    @RequireApiKey
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnvioDTO crearEnvio(@Valid @RequestBody CrearEnvioDTO dto) {
        // Cuando tengamos usuarios re tiene que pillas la api key, un servicio get id by api key y pasarla aqui
        return envioService.crearEnvio(dto, 1); // TODO: Usuario simulado con ID 1; obtener ID de usuario autenticado
    }
}
