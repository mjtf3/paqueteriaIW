package com.paqueteria.controller;

import java.time.LocalDate;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.paqueteria.dto.UsuarioData;
import com.paqueteria.model.TipoEnum;
import com.paqueteria.services.UsuarioService;

import jakarta.validation.Valid;

@RequestMapping("/auth")
@Controller
@RegisterReflectionForBinding(UsuarioData.class)
public class LoginController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/registro")
    public String registrarForm() {
        return "registroForm";
    }

    @PostMapping("/registro")
    public String registrarTienda(@Valid @ModelAttribute("usuario") UsuarioData usuarioData, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
        }
        if (usuarioService.findByCorreo(usuarioData.getCorreo()) != null) {
            model.addAttribute("error", "Ya existe un usuario con ese correo");
        }

        usuarioData.setTipo(TipoEnum.CLIENTE);
        usuarioData.setFechaCreacion(LocalDate.now());
        UsuarioData usuarioRegistrado;

        try {
            usuarioRegistrado = usuarioService.registrar(usuarioData);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuarioData);
            return "registroForm";
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/dashboard")
    public String redirectUser(Authentication authentication, Model model) {
        String correo = authentication.getName();
        UsuarioData usuario = usuarioService.findByCorreo(correo);
        switch (usuario.getTipo()) {
            case CLIENTE:
                return "redirect:/";
            case WEBMASTER:
                return "redirect:/";
            case REPARTIDOR:
                return "redirect:/";
            default:
                return "redirect:/";
        }
    }

}
