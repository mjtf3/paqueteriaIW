package com.paqueteria.controller;
import com.paqueteria.services.HistorialRutaService;
import com.paqueteria.model.Ruta;
import com.paqueteria.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.paqueteria.dto.GrupoHistorial;
import com.paqueteria.dto.EnvioDTO;
import com.paqueteria.services.EnvioService;
import com.paqueteria.services.RutaService;
import com.paqueteria.repository.UsuarioRepository;
import com.paqueteria.model.TipoEnum;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.thymeleaf.expression.Lists;


@Controller
@RegisterReflectionForBinding({ GrupoHistorial.class, EnvioDTO.class, Ruta.class, Usuario.class, ArrayList.class, Lists.class })
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
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (esWebmaster) {
            var rutas = historialRutaService.obtenerHistorialWebmaster();

            // 1. Procesar Conteos y Fechas individuales (como ya hacías)
            var rutaEnviosCount = new HashMap<Integer, Integer>();
            var rutaFechaMap = new HashMap<Integer, String>();
            for (Ruta r : rutas) {
                if (r != null && r.getId() != null) {
                    var lista = envioService.obtenerEnviosPorRuta(r.getId());
                    rutaEnviosCount.put(r.getId(), lista == null ? 0 : lista.size());
                    rutaFechaMap.put(r.getId(), r.getFecha() == null ? "" : r.getFecha().format(fmt));
                }
            }
            model.addAttribute("rutaEnviosCount", rutaEnviosCount);
            model.addAttribute("rutaFechaMap", rutaFechaMap);

            // 2. AGRUPAR POR REPARTIDOR (Convertir Map a List de GrupoHistorial)
            Map<String, List<Ruta>> mapRepartidor = rutas.stream()
                    .filter(r -> r.getUsuario() != null)
                    .collect(Collectors.groupingBy(r -> r.getUsuario().getNombre() + " " + r.getUsuario().getApellidos()));
            
            List<GrupoHistorial> gruposPorRepartidor = new ArrayList<>();
            mapRepartidor.forEach((nombre, lista) -> gruposPorRepartidor.add(new GrupoHistorial(nombre, lista)));
            model.addAttribute("gruposPorRepartidor", gruposPorRepartidor);

            // 3. AGRUPAR POR FECHA (Convertir Map a List de GrupoHistorial)
            var mapFecha = rutas.stream()
                    .filter(r -> r.getFecha() != null)
                    .collect(Collectors.groupingBy(Ruta::getFecha, TreeMap::new, Collectors.toList()));
            
            List<GrupoHistorial> gruposPorFecha = new ArrayList<>();
            // Usamos descendingMap para que las fechas más recientes salgan primero
            ((TreeMap<LocalDate, List<Ruta>>) mapFecha).descendingMap().forEach((fecha, lista) -> {
                gruposPorFecha.add(new GrupoHistorial(fecha.format(fmt), lista));
            });
            model.addAttribute("gruposPorFecha", gruposPorFecha);

            model.addAttribute("mode", (mode == null || mode.isBlank()) ? "fecha" : mode);

        } else if (usuario != null) {
            // Lógica para Repartidor (ya estaba bien, pero usamos rutaFechaMap para seguridad)
            var rutas = historialRutaService.obtenerHistorialRepartidor(usuario);
            var rutaEnviosCount = new HashMap<Integer, Integer>();
            var rutaFechaMap = new HashMap<Integer, String>();
            for (Ruta r : rutas) {
                if (r != null && r.getId() != null) {
                    var lista = envioService.obtenerEnviosPorRuta(r.getId());
                    rutaEnviosCount.put(r.getId(), lista == null ? 0 : lista.size());
                    rutaFechaMap.put(r.getId(), r.getFecha() == null ? "" : r.getFecha().format(fmt));
                }
            }
            model.addAttribute("rutaEnviosCount", rutaEnviosCount);
            model.addAttribute("rutaFechaMap", rutaFechaMap);
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
