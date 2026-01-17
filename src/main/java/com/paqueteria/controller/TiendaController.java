package com.paqueteria.controller;

import com.paqueteria.dto.UsuarioData;
import com.paqueteria.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tienda")
public class TiendaController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/perfil")
    public String verPerfil(Authentication authentication, Model model) {
        String correo = authentication.getName();
        UsuarioData usuario = usuarioService.findByCorreo(correo);
        model.addAttribute("usuario", usuario);
        return "tiendaPerfil";
    }

    @GetMapping("/editar")
    public String editarForm(Authentication authentication, Model model) {
        String correo = authentication.getName();
        UsuarioData usuario = usuarioService.findByCorreo(correo);
        model.addAttribute("usuario", usuario);
        return "tiendaEditar";
    }

    @PostMapping("/editar")
    public String actualizarDatos(@Valid @ModelAttribute("usuario") UsuarioData usuarioData,
                                   BindingResult result,
                                   Authentication authentication,
                                   Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "tiendaEditar";
        }

        String correo = authentication.getName();
        
        try {
            usuarioService.actualizarDatos(correo, usuarioData);
            return "redirect:/tienda/perfil";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "tiendaEditar";
        }
    }
}
