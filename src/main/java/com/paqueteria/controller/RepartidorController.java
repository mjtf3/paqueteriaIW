package com.paqueteria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.paqueteria.services.EnvioService;
import com.paqueteria.services.UsuarioService;
import com.paqueteria.dto.UsuarioData;

@Controller
@RequestMapping("/repartidor")
public class RepartidorController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EnvioService envioService;

    @GetMapping("/inicio")
    public String inicioRepartidor(Authentication authentication, Model model) {
        String correo = authentication.getName();
        UsuarioData usuario = usuarioService.findByCorreo(correo);
        model.addAttribute("repartidorId", usuario.getId());
        model.addAttribute("nombre", usuario.getNombre());
        model.addAttribute("numPaquetes", envioService.contarPaquetesPendientesRepartidor(usuario.getId()));
        return "inicioRepartidor";
    }
}
