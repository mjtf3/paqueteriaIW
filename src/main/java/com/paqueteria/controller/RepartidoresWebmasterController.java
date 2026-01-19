package com.paqueteria.controller;

import com.paqueteria.dto.RepartidorDTO;
import com.paqueteria.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/webmaster/repartidores")
public class RepartidoresWebmasterController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarRepartidores(Model model) {
        List<RepartidorDTO> repartidores = usuarioService.getAllRepartidores();
        model.addAttribute("repartidores", repartidores);
        model.addAttribute("nuevoRepartidor", new RepartidorDTO());
        return "repartidoresWebmaster";
    }

    @PostMapping("/crear")
    public String crearRepartidor(@ModelAttribute RepartidorDTO repartidorDTO, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.crearRepartidor(repartidorDTO);
            redirectAttributes.addFlashAttribute("success", "Repartidor creado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear repartidor: " + e.getMessage());
        }
        return "redirect:/webmaster/repartidores";
    }
    
    @PostMapping("/editar")
    public String editarRepartidor(@ModelAttribute RepartidorDTO repartidorDTO, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.editarRepartidor(repartidorDTO);
            redirectAttributes.addFlashAttribute("success", "Repartidor actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar repartidor: " + e.getMessage());
        }
        return "redirect:/webmaster/repartidores";
    }

    @PostMapping("/desactivar")
    public String desactivarRepartidor(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.desactivarRepartidor(id);
            redirectAttributes.addFlashAttribute("success", "Repartidor desactivado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar repartidor: " + e.getMessage());
        }
        return "redirect:/webmaster/repartidores";
    }
}
