package com.paqueteria.controller;

import com.paqueteria.dto.UsuarioData;
import com.paqueteria.services.ApiService;
import com.paqueteria.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/tienda/{id}")
@Controller
public class TiendaController {

    @Autowired
    private ApiService apiService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/apikey")
    public String apiPage(@PathVariable(value = "id") Integer idTienda, Authentication authentication, Model model) {
        UsuarioData usuarioData = usuarioService.findByCorreo(authentication.getName());
        if (!idTienda.equals(usuarioData.getId())) {
            return "redirect:/tienda/" + usuarioData.getId() + "/apikey";
        }
        model.addAttribute("apiKeys",usuarioService.getAPIs(usuarioData));
        return "apiKeyView";
    }

//    @PostMapping("/apikey")
//    public String createApi() {
//        return "createApi";
//    }

//    @PostMapping("/apikey/delete/{id}")
//    public String deleteApi() {
//        return "deleteApi";
//    }

}
