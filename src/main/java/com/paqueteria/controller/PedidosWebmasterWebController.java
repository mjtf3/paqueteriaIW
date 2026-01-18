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

        // Añadir banderas de listas vacías (evitar problemas de reflexión en GraalVM)
        model.addAttribute("ausentesVacio", ausentesPage.getContent().isEmpty());
        model.addAttribute("rechazadosVacio", rechazadosPage.getContent().isEmpty());
        model.addAttribute("pendientesVacio", pendientesPage.getContent().isEmpty());

        // Añadir valores actuales de paginación para mantener el estado
        model.addAttribute("pageAusentes", pageAusentes);
        model.addAttribute("pageRechazados", pageRechazados);
        model.addAttribute("pagePendientes", pagePendientes);

        // Calcular información de paginación para Ausentes (evitar operaciones en template)
        model.addAttribute("ausentesTienePaginacion", ausentesPage.getTotalPages() > 1);
        model.addAttribute("ausentesTienePrevio", ausentesPage.getNumber() > 0);
        model.addAttribute("ausentesPaginaPrevia", ausentesPage.getNumber() - 1);
        model.addAttribute("ausentesTieneSiguiente", ausentesPage.getNumber() < ausentesPage.getTotalPages() - 1);
        model.addAttribute("ausentesPaginaSiguiente", ausentesPage.getNumber() + 1);
        model.addAttribute("ausentesPaginaActual", ausentesPage.getNumber());

        // Generar lista de números de página para Ausentes
        List<Integer> ausentesPaginas = new java.util.ArrayList<>();
        for (int i = 0; i < ausentesPage.getTotalPages(); i++) {
            ausentesPaginas.add(i);
        }
        model.addAttribute("ausentesPaginas", ausentesPaginas);

        // Calcular información de paginación para Rechazados
        model.addAttribute("rechazadosTienePaginacion", rechazadosPage.getTotalPages() > 1);
        model.addAttribute("rechazadosTienePrevio", rechazadosPage.getNumber() > 0);
        model.addAttribute("rechazadosPaginaPrevia", rechazadosPage.getNumber() - 1);
        model.addAttribute("rechazadosTieneSiguiente", rechazadosPage.getNumber() < rechazadosPage.getTotalPages() - 1);
        model.addAttribute("rechazadosPaginaSiguiente", rechazadosPage.getNumber() + 1);
        model.addAttribute("rechazadosPaginaActual", rechazadosPage.getNumber());

        // Generar lista de números de página para Rechazados
        List<Integer> rechazadosPaginas = new java.util.ArrayList<>();
        for (int i = 0; i < rechazadosPage.getTotalPages(); i++) {
            rechazadosPaginas.add(i);
        }
        model.addAttribute("rechazadosPaginas", rechazadosPaginas);

        // Calcular información de paginación para Pendientes
        model.addAttribute("pendientesTienePaginacion", pendientesPage.getTotalPages() > 1);
        model.addAttribute("pendientesTienePrevio", pendientesPage.getNumber() > 0);
        model.addAttribute("pendientesPaginaPrevia", pendientesPage.getNumber() - 1);
        model.addAttribute("pendientesTieneSiguiente", pendientesPage.getNumber() < pendientesPage.getTotalPages() - 1);
        model.addAttribute("pendientesPaginaSiguiente", pendientesPage.getNumber() + 1);
        model.addAttribute("pendientesPaginaActual", pendientesPage.getNumber());

        // Generar lista de números de página para Pendientes
        List<Integer> pendientesPaginas = new java.util.ArrayList<>();
        for (int i = 0; i < pendientesPage.getTotalPages(); i++) {
            pendientesPaginas.add(i);
        }
        model.addAttribute("pendientesPaginas", pendientesPaginas);

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
