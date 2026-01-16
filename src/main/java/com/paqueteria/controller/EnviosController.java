package com.paqueteria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.paqueteria.dto.CrearEnvioDTO;
import com.paqueteria.dto.EnvioDTO;
import com.paqueteria.service.EnvioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/envios")
public class EnviosController {

    @Autowired
    private EnvioService envioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnvioDTO crearEnvio(@Valid @RequestBody CrearEnvioDTO dto) {
        return envioService.crearEnvio(dto, 1); // Usuario simulado con ID 1
    }
}
