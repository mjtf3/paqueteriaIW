package com.paqueteria.controller;

import com.paqueteria.dto.UsuarioData;
import com.paqueteria.services.UsuarioService;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/webmaster/tiendas")
@RegisterReflectionForBinding({UsuarioData.class})
public class TiendasWebmasterWebController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String tiendas(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        int pageSize = 10;

        // Obtener tiendas paginadas
        Page<UsuarioData> tiendasPage = usuarioService.getTiendasPaginadas(page, pageSize);

        // Añadir datos al modelo
        model.addAttribute("tiendas", tiendasPage);
        model.addAttribute("tiendasContent", tiendasPage.getContent());

        // Añadir banderas de listas vacías (evitar problemas de reflexión en GraalVM)
        model.addAttribute("tiendasVacio", tiendasPage.getContent().isEmpty());

        // Añadir valor actual de paginación
        model.addAttribute("page", page);

        // Calcular información de paginación (evitar operaciones en template)
        model.addAttribute("tieneTiendasPaginacion", tiendasPage.getTotalPages() > 1);
        model.addAttribute("tieneTiendasPrevio", tiendasPage.getNumber() > 0);
        model.addAttribute("tiendasPaginaPrevia", tiendasPage.getNumber() - 1);
        model.addAttribute("tieneTiendasSiguiente", tiendasPage.getNumber() < tiendasPage.getTotalPages() - 1);
        model.addAttribute("tiendasPaginaSiguiente", tiendasPage.getNumber() + 1);
        model.addAttribute("tiendasPaginaActual", tiendasPage.getNumber());

        // Generar lista de números de página
        List<Integer> tiendasPaginas = new ArrayList<>();
        for (int i = 0; i < tiendasPage.getTotalPages(); i++) {
            tiendasPaginas.add(i);
        }
        model.addAttribute("tiendasPaginas", tiendasPaginas);

        return "tiendasWebmaster";
    }

    @PostMapping("/eliminar")
    @ResponseBody
    public String eliminarTienda(@RequestParam Integer tiendaId) {
        try {
            usuarioService.eliminarTienda(tiendaId);
            return "OK";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @GetMapping("/{tiendaId}/info/data")
    @ResponseBody
    public UsuarioData obtenerDatosTienda(@PathVariable Integer tiendaId) {
        try {
            UsuarioData tienda = usuarioService.findById(tiendaId);
            if (tienda == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tienda no encontrada");
            }
            return tienda;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener datos de la tienda");
        }
    }
}
