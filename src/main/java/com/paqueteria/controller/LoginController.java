package com.paqueteria.controller;

import com.paqueteria.dto.UsuarioData;
import com.paqueteria.model.TipoEnum;
import com.paqueteria.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import java.time.LocalDate;


@Controller
public class LoginController {
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/auth/registro")
    public String registrarForm(){
        return "registroForm";
    }

    @PostMapping("/auth/registro")
    public String registrarTienda(@Valid @ModelAttribute("usuario") UsuarioData usuarioData, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("errors", result.getAllErrors());
        }
        if (usuarioService.findByCorreo(usuarioData.getCorreo()) != null){
            model.addAttribute("error", "Ya existe un usuario con ese correo");
        }

        UsuarioData usuario = new UsuarioData();
        usuario.setCorreo(usuarioData.getCorreo());
        usuario.setContrasena(usuarioData.getContrasena());
        usuario.setNombre(usuarioData.getNombre());
        usuario.setTelefono(usuarioData.getTelefono());
        usuario.setApodo(usuarioData.getApodo());
        usuario.setNombreTienda(usuarioData.getNombreTienda());
        usuario.setApellidos(usuarioData.getApellidos());
        usuario.setTipo(TipoEnum.CLIENTE);
        usuario.setFechaCreacion(LocalDate.now());
        UsuarioData usuarioRegistrado;

        try{
            usuarioRegistrado = usuarioService.registrar(usuario);
        }catch(Exception e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuarioData);
            return "registroForm";
        }
        return "index";
    }
}
