package com.paqueteria.controller;
import com.paqueteria.services.HistorialRutaService;
import com.paqueteria.model.Ruta;
import com.paqueteria.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Map;
import java.util.stream.Collectors;


import com.paqueteria.dto.EnvioDTO;
import com.paqueteria.services.EnvioService;
import com.paqueteria.services.RutaService;
import com.paqueteria.model.Ruta;
import com.paqueteria.model.Usuario;
import com.paqueteria.repository.UsuarioRepository;
import com.paqueteria.model.TipoEnum;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


@Controller
public class RutaController {
                
            

    @Autowired
    private EnvioService envioService;

    @Autowired
    private RutaService rutaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

        @Autowired
    private HistorialRutaService historialRutaService;
    @GetMapping({"/webmaster/historial", "/repartidor/historial"})
    public String mostrarHistorial(Model model,
                                   @RequestParam(value = "mode", required = false) String mode) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = null;
        if (auth != null && auth.getName() != null) {
            usuario = usuarioRepository.findByCorreo(auth.getName()).orElse(null);
        }
        boolean esWebmaster = usuario != null && usuario.getTipo() == TipoEnum.WEBMASTER;
        if (esWebmaster) {
            // Agrupar rutas por repartidor
            var rutas = historialRutaService.obtenerHistorialWebmaster();
            // Mapa con el conteo de envíos por ruta para usar en las plantillas
            Map<Integer, Integer> rutaEnviosCount = rutas.stream()
                .collect(Collectors.toMap(r -> r.getId(), r -> (r.getEnvios() == null ? 0 : r.getEnvios().size())));
            model.addAttribute("rutaEnviosCount", rutaEnviosCount);
            Map<String, List<Ruta>> rutasPorRepartidor = rutas.stream()
                    .filter(r -> r.getUsuario() != null)
                    .collect(Collectors.groupingBy(r -> r.getUsuario().getNombre() + " " + r.getUsuario().getApellidos()));
            model.addAttribute("rutasPorRepartidor", rutasPorRepartidor);

                var repartidores = usuarioRepository.findAll().stream()
                    .filter(u -> u.getTipo() == com.paqueteria.model.TipoEnum.REPARTIDOR)
                    .toList();
                model.addAttribute("repartidores", repartidores);

                var rutasPorFechaTmp = rutas.stream()
                    .filter(r -> r.getFecha() != null)
                    .collect(Collectors.groupingBy(Ruta::getFecha));
                var rutasPorFecha = rutasPorFechaTmp.entrySet().stream()
                    .sorted(Map.Entry.<java.time.LocalDate, List<Ruta>>comparingByKey(java.util.Comparator.reverseOrder()) )
                    .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        java.util.LinkedHashMap::new
                    ));
                model.addAttribute("rutasPorFecha", rutasPorFecha);
            // Control de modo: 'fecha' o 'nombre'
            model.addAttribute("mode", (mode == null || mode.isBlank()) ? "fecha" : mode);


        } else if (usuario != null) {
            var rutas = historialRutaService.obtenerHistorialRepartidor(usuario);
            Map<Integer, Integer> rutaEnviosCount = rutas.stream()
                    .collect(Collectors.toMap(r -> r.getId(), r -> (r.getEnvios() == null ? 0 : r.getEnvios().size())));
            model.addAttribute("rutaEnviosCount", rutaEnviosCount);
            model.addAttribute("rutasUsuario", rutas);
        }
        model.addAttribute("esWebmaster", esWebmaster);
        return "historial";
    }

    @GetMapping("/repartidor/ruta")
    public String mostrarRuta(@RequestParam(value = "repartidorId", required = false) Long repartidorId, Model model) {
        // Si no se pasa repartidorId intentar inferirlo del usuario autenticado
        if (repartidorId == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null) {
                var userOpt = usuarioRepository.findByCorreo(auth.getName());
                if (userOpt.isPresent()) {
                    repartidorId = Long.valueOf(userOpt.get().getId());
                } else {
                    return "redirect:/";
                }
            } else {
                return "redirect:/";
            }
        }

        List<EnvioDTO> envios = envioService.obtenerEnviosPorRepartidor(repartidorId);

        // Crear o recuperar la ruta del día para este repartidor y asignar los envíos activos
        Usuario usuario = usuarioRepository.findById(repartidorId.intValue()).orElse(null);
        if (usuario != null) {
            var optRuta = rutaService.buscarRutaPorUsuarioYFecha(usuario, LocalDate.now());
            if (optRuta.isEmpty()) {
                Ruta nueva = new Ruta(LocalDate.now(), usuario);
                Ruta rutaGuardada = rutaService.guardarRuta(nueva);
                envioService.asignarRutaAEnviosActivosDelRepartidor(repartidorId.intValue(), rutaGuardada);
            } else {
                // Asegurar que los envíos activos están vinculados a la ruta existente
                envioService.asignarRutaAEnviosActivosDelRepartidor(repartidorId.intValue(), optRuta.get());
            }
            // refrescar la lista de envíos activos tras la asignación
            envios = envioService.obtenerEnviosPorRepartidor(repartidorId);
        }

        model.addAttribute("envios", envios);
        model.addAttribute("enviosCount", envios == null ? 0 : envios.size());
        model.addAttribute("repartidorId", repartidorId);
        return "ruta";
    }

    @GetMapping("/repartidor/ruta/finalizar")
    public String mostrarResumenGet(@RequestParam(value = "repartidorId", required = false) Long repartidorId, Model model) {
        // Si no se pasa repartidorId intentar inferirlo del usuario autenticado
        if (repartidorId == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null) {
                var userOpt = usuarioRepository.findByCorreo(auth.getName());
                if (userOpt.isPresent()) {
                    repartidorId = Long.valueOf(userOpt.get().getId());
                } else {
                    return "redirect:/";
                }
            } else {
                return "redirect:/";
            }
        }
        // Intentar obtener la ruta del día para este repartidor
        Usuario usuario = usuarioRepository.findById(repartidorId.intValue()).orElse(null);
        List<EnvioDTO> envios;
        if (usuario != null) {
            var optRuta = rutaService.buscarRutaPorUsuarioYFecha(usuario, LocalDate.now());
            if (optRuta.isPresent()) {
                // Contar todos los envíos asignados a la ruta (incluye ENTREGADO/AUSENTE/RECHAZADO)
                envios = envioService.obtenerEnviosPorRuta(optRuta.get().getId());
            } else {
                // Fallback: usar todos los envíos del repartidor (todos los estados)
                envios = envioService.obtenerEnviosPorRepartidorTodosEstados(repartidorId);
            }
        } else {
            envios = envioService.obtenerEnviosPorRepartidorTodosEstados(repartidorId);
        }

        int totalEntregados = 0;
        int totalRechazados = 0;
        int totalAusentes = 0;
        int totalNoEntregados = 0;
        int totalPaquetes = envios.size();
        for (EnvioDTO envio : envios) {
            switch (envio.getEstado()) {
                case ENTREGADO:
                    totalEntregados++;
                    break;
                case RECHAZADO:
                    totalRechazados++;
                    break;
                case AUSENTE:
                    totalAusentes++;
                    break;
                case PENDIENTE:
                case RUTA:
                    totalNoEntregados++;
                    break;
                default:
                    break;
            }
        }

        model.addAttribute("totalEntregados", totalEntregados);
        model.addAttribute("totalRechazados", totalRechazados);
        model.addAttribute("totalAusentes", totalAusentes);
        model.addAttribute("totalNoEntregados", totalNoEntregados);
        model.addAttribute("totalPaquetes", totalPaquetes);
        model.addAttribute("enviosCount", envios == null ? 0 : envios.size());
        // Exponer si el usuario autenticado es webmaster para la plantilla
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean esWebmaster = false;
        if (auth != null && auth.getName() != null) {
            var userOpt = usuarioRepository.findByCorreo(auth.getName());
            esWebmaster = userOpt.map(u -> u.getTipo() == TipoEnum.WEBMASTER).orElse(false);
        }
        model.addAttribute("esWebmaster", esWebmaster);
        return "resumenRuta";
    }

    @GetMapping({"/repartidor/ruta/resumen", "/ruta/resumen", "/webmaster/ruta/resumen"})
    public String mostrarResumenPorRutaId(@RequestParam("rutaId") Integer rutaId, Model model) {
        List<EnvioDTO> envios = envioService.obtenerEnviosPorRuta(rutaId);

        int totalEntregados = 0;
        int totalRechazados = 0;
        int totalAusentes = 0;
        int totalNoEntregados = 0;
        int totalPaquetes = envios.size();
        for (EnvioDTO envio : envios) {
            switch (envio.getEstado()) {
                case ENTREGADO:
                    totalEntregados++;
                    break;
                case RECHAZADO:
                    totalRechazados++;
                    break;
                case AUSENTE:
                    totalAusentes++;
                    break;
                case PENDIENTE:
                case RUTA:
                    totalNoEntregados++;
                    break;
                default:
                    break;
            }
        }

        model.addAttribute("totalEntregados", totalEntregados);
        model.addAttribute("totalRechazados", totalRechazados);
        model.addAttribute("totalAusentes", totalAusentes);
        model.addAttribute("totalNoEntregados", totalNoEntregados);
        model.addAttribute("totalPaquetes", totalPaquetes);
        model.addAttribute("enviosCount", envios == null ? 0 : envios.size());
        // Exponer si el usuario autenticado es webmaster para la plantilla
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean esWebmaster = false;
        if (auth != null && auth.getName() != null) {
            var userOpt = usuarioRepository.findByCorreo(auth.getName());
            esWebmaster = userOpt.map(u -> u.getTipo() == TipoEnum.WEBMASTER).orElse(false);
        }
        model.addAttribute("esWebmaster", esWebmaster);
        return "resumenRuta";
    }
}
