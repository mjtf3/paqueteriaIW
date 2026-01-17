package com.paqueteria.controller;

import com.paqueteria.dto.ApiData;
import com.paqueteria.dto.UsuarioData;
import com.paqueteria.services.ApiService;
import com.paqueteria.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequestMapping("/tienda/{id}")
@Controller
public class TiendaController {

    @Autowired
    private ApiService apiService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/apikey")
    public String apiPage(@PathVariable(value = "id") Integer idTienda, Authentication authentication, Model model, HttpSession session) {
        UsuarioData usuarioData = usuarioService.findByCorreo(authentication.getName());
        if (!idTienda.equals(usuarioData.getId())) {
            return "redirect:/tienda/" + usuarioData.getId() + "/apikey";
        }
        model.addAttribute("tienda", usuarioData.getId());
        model.addAttribute("apiKeys",usuarioService.getAPIs(usuarioData));
        ApiData newApi = (ApiData) session.getAttribute("newApi");
        if (newApi != null) {
            model.addAttribute("newApi", newApi);
        }
        return "apiKeyView";
    }

    @PostMapping("/apikey")
    public String createApi(@PathVariable(value = "id") Integer idTienda, Authentication authentication, String nombre, Model model,HttpSession session) {
        UsuarioData usuarioData = usuarioService.findByCorreo(authentication.getName());
        if (!idTienda.equals(usuarioData.getId())) {
            return "redirect:/tienda/" + usuarioData.getId() + "/apikey";
        }
        ApiData apiData = new ApiData();
        apiData.setNombre(nombre);
        apiData.setFecha(LocalDate.now());
        apiData.setKey("holaa");
        usuarioService.addApi(usuarioData,apiData);

        session.setAttribute("newApi",apiData);

        return "redirect:/tienda/" + usuarioData.getId() + "/apikey";
    }

    //Para que cuando cierre el pop up ya se borre los datos del new api
    @PostMapping("/borrarNewApi")
    @ResponseBody
    public String borrarNewApi(@PathVariable(value = "id") Integer idTienda,Authentication authentication,HttpSession session){
        UsuarioData usuarioData = usuarioService.findByCorreo(authentication.getName());
        if (!idTienda.equals(usuarioData.getId())) {
            return "error: no autorizado";
        }
        session.removeAttribute("newApi");
        return "ok";
    }

    @PostMapping("/apikey/delete/{apiKeyId}")
    public String deleteApi(@PathVariable(value = "id") Integer idTienda,@PathVariable(value = "apiKeyId") Integer idApi,Authentication authentication,HttpSession session) {
        UsuarioData usuarioData = usuarioService.findByCorreo(authentication.getName());
        if (!idTienda.equals(usuarioData.getId())) {
            return "redirect:/tienda/" + usuarioData.getId() + "/apikey";
        }
        ApiData apiData = apiService.findById(idApi);
        usuarioService.removeApi(usuarioData,apiData);
        return "redirect:/tienda/" + usuarioData.getId() + "/apikey";

    }

}
