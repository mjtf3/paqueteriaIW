package com.paqueteria.controller;

import com.paqueteria.dto.EnvioDTO;
import com.paqueteria.dto.RepartidorDTO;
import com.paqueteria.model.EstadoEnum;
import com.paqueteria.services.EnvioService;
import com.paqueteria.services.UsuarioService;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/webmaster/pedidos")
@RegisterReflectionForBinding({EnvioDTO.class, RepartidorDTO.class})
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

        // Obtener envíos por estado con paginación (ahora devuelven DTOs)
        Page<EnvioDTO> ausentesPage = envioService.getEnviosPorEstado(EstadoEnum.AUSENTE, pageAusentes, pageSize);
        Page<EnvioDTO> rechazadosPage = envioService.getEnviosPorEstado(EstadoEnum.RECHAZADO, pageRechazados, pageSize);
        Page<EnvioDTO> pendientesPage = envioService.getEnviosPorEstado(EstadoEnum.PENDIENTE, pagePendientes, pageSize);

        // Obtener todos los repartidores activos (ahora devuelve DTOs)
        List<RepartidorDTO> repartidores = usuarioService.getRepartidoresActivos();

        // Añadir datos al modelo - pasamos tanto el Page completo como su contenido
        model.addAttribute("ausentes", ausentesPage);
        model.addAttribute("ausentesContent", ausentesPage.getContent());
        model.addAttribute("rechazados", rechazadosPage);
        model.addAttribute("rechazadosContent", rechazadosPage.getContent());
        model.addAttribute("pendientes", pendientesPage);
        model.addAttribute("pendientesContent", pendientesPage.getContent());
        model.addAttribute("repartidores", repartidores);

        // Añadir valores actuales de paginación para mantener el estado
        model.addAttribute("pageAusentes", pageAusentes);
        model.addAttribute("pageRechazados", pageRechazados);
        model.addAttribute("pagePendientes", pagePendientes);

        // Añadir fecha actual para comparaciones (evitar T(java.time.LocalDate) en templates)
        model.addAttribute("fechaActual", java.time.LocalDate.now());

        return "pedidosWebmaster";
    }

    @PostMapping("/asignar")
    @ResponseBody
    public String asignarRepartidor(@RequestParam Integer envioId, @RequestParam Integer repartidorId) {
        try {
            envioService.asignarRepartidor(envioId, repartidorId);
            return "OK";
        } catch (org.springframework.web.server.ResponseStatusException e) {
            // Extraer solo el mensaje de error sin el código HTTP
            return "ERROR: " + e.getReason();
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
