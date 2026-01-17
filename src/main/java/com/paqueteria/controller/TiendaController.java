package com.paqueteria.controller;

import com.paqueteria.dto.ApiData;
import com.paqueteria.dto.UsuarioData;
import com.paqueteria.services.ApiService;
import com.paqueteria.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.paqueteria.utils.generadorCadenas;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;

@RegisterReflectionForBinding({UsuarioData.class,ApiData.class})
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
        model.addAttribute("apiKeys", usuarioService.getAPIs(usuarioData) != null ? usuarioService.getAPIs(usuarioData) : new ArrayList<>());
        model.addAttribute("apis", usuarioService.getAPIs(usuarioData).isEmpty());
        ApiData newApi = (ApiData) session.getAttribute("newApi");
        if (newApi != null) {
            model.addAttribute("newApiNombre", newApi.getNombre());
            model.addAttribute("newApiKey", newApi.getKey());
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
        apiData.setKey(generadorCadenas.generarCadena());
        usuarioService.addApi(usuarioData,apiData);

        model.addAttribute("newApiNombre", apiData.getNombre());
        model.addAttribute("newApiKey", apiData.getKey());

        session.setAttribute("newApi",apiData);

        return "redirect:/tienda/" + usuarioData.getId() + "/apikey";
    }

    //Para que cuando cierre el pop up ya se borre los datos del new api
    @PostMapping("/borrarNewApi")
    @ResponseBody
    public String borrarNewApi(@PathVariable(value = "id") Integer idTienda, Authentication authentication, HttpSession session){
        UsuarioData usuarioData = usuarioService.findByCorreo(authentication.getName());

        if (!idTienda.equals(usuarioData.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
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
        if (apiData != null) {
            return "redirect:/tienda/" + usuarioData.getId() + "/apikey";
        }
        boolean ownsApi = usuarioService.getAPIs(usuarioData)
                .stream()
                .anyMatch(api -> api.getId().equals(idApi));
        if (!ownsApi) {
            return "redirect:/tienda/" + usuarioData.getId() + "/apikey";
        }
        usuarioService.removeApi(usuarioData,apiData);
        return "redirect:/tienda/" + usuarioData.getId() + "/apikey";

    }

}
