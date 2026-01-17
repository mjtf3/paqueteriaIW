package com.paqueteria.controller;

import com.paqueteria.model.Envio;
import com.paqueteria.model.EstadoEnum;
import com.paqueteria.model.Usuario;
import com.paqueteria.service.EnvioService;
import com.paqueteria.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/webmaster/pedidos")
public class PedidosWebmasterWebController {

    @Autowired
    private EnvioService envioService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String pedidos(
            @RequestParam(defaultValue = "0") int pageAusentes,
            @RequestParam(defaultValue = "0") int pageRechazados,
            @RequestParam(defaultValue = "0") int pagePendientes,
            Model model) {

        int pageSize = 10;

        // Obtener envíos por estado con paginación
        Page<Envio> ausentes = envioService.getEnviosPorEstado(EstadoEnum.AUSENTE, pageAusentes, pageSize);
        Page<Envio> rechazados = envioService.getEnviosPorEstado(EstadoEnum.RECHAZADO, pageRechazados, pageSize);
        Page<Envio> pendientes = envioService.getEnviosPorEstado(EstadoEnum.PENDIENTE, pagePendientes, pageSize);

        // Obtener lista de repartidores activos
        List<Usuario> repartidores = usuarioService.getRepartidoresActivos();

        // Añadir datos al modelo
        model.addAttribute("ausentes", ausentes);
        model.addAttribute("rechazados", rechazados);
        model.addAttribute("pendientes", pendientes);
        model.addAttribute("repartidores", repartidores);

        return "pedidosWebmaster";
    }

    @PostMapping("/asignar")
    @ResponseBody
    public String asignarRepartidor(@RequestParam Integer envioId, @RequestParam Integer repartidorId) {
        try {
            envioService.asignarRepartidor(envioId, repartidorId);
            return "OK";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}

