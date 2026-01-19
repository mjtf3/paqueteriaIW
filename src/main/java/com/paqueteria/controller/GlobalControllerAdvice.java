package com.paqueteria.controller;

import com.paqueteria.dto.UsuarioData;
import com.paqueteria.services.UsuarioService;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RegisterReflectionForBinding({UsuarioData.class})
public class GlobalControllerAdvice {

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute
    public void addUsuarioToModel(Authentication authentication, Model model) {
        if (
            authentication != null &&
            authentication.isAuthenticated() &&
            !"anonymousUser".equals(authentication.getPrincipal())
        ) {
            String correo = authentication.getName();
            UsuarioData usuario = usuarioService.findByCorreo(correo);

            if (usuario != null) {
                model.addAttribute("usuarioActual", usuario);
            }
        }
    }
}
