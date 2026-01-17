package com.paqueteria.controller;

import com.paqueteria.dto.UsuarioData;
import com.paqueteria.model.TipoEnum;
import com.paqueteria.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@RequestMapping("/auth")
@Controller
public class LoginController {
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/registro")
    public String registrarForm(){
        return "registroForm";
    }

    @PostMapping("/registro")
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
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/dashboard")
    public String redirectUser(Authentication authentication, Model model){
        String correo = authentication.getName();
        UsuarioData usuario = usuarioService.findByCorreo(correo);
        switch(usuario.getTipo()){
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

//    @PostMapping("/login")
//    public String login(@Valid @ModelAttribute("loginData") LoginData loginData, BindingResult result, Model model){
//        if (result.hasErrors()){
//            // Obtén el primer error como string
//            String errorMsg = result.getAllErrors().stream()
//                    .map(e -> e.getDefaultMessage())
//                    .findFirst()
//                    .orElse("Error de validación");
//            model.addAttribute("error", errorMsg);
//            return "loginForm";
//        }
//    else{
//        UsuarioService.LoginStatus loginStatus = usuarioService.login(loginData.getCorreo(),loginData.getContrasena());
//        if (loginStatus == UsuarioService.LoginStatus.LOGIN_OK){
//            UsuarioData usuario = usuarioService.findByCorreo(loginData.getCorreo());
//            if (usuario.getTipo() == TipoEnum.CLIENTE){
//                return "redirect:/";
//            }
//        }
//        else if (loginStatus == UsuarioService.LoginStatus.USER_NOT_FOUND){
//            model.addAttribute("error", "Usuario no encontrado");
//            return "loginForm";
//        }
//        else if (loginStatus == UsuarioService.LoginStatus.ERROR_PASSWORD){
//            model.addAttribute("error", "Contraseña incorrecta");
//            return "loginForm";
//        }
//        return  "loginForm";
//    }
//    }

}
