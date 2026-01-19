package com.paqueteria.controller;
import com.paqueteria.services.HistorialRutaService;
import com.paqueteria.model.Ruta;
import com.paqueteria.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
@RequestMapping("/repartidor/ruta")
public class RutaController {
    private static final Logger logger = LoggerFactory.getLogger(RutaController.class);
                
            

    @Autowired
    private EnvioService envioService;

    @Autowired
    private RutaService rutaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

        @Autowired
    private HistorialRutaService historialRutaService;
    @GetMapping("/historial")
    public String mostrarHistorial(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = null;
            if (auth != null && auth.getName() != null) {
                // auth.getPrincipal() is a UserDetails, so buscamos el Usuario por correo
            usuario = usuarioRepository.findByCorreo(auth.getName()).orElse(null);
            logger.info("Historial: principal={} -> usuarioFound={}", auth.getName(), usuario != null);
        }
            boolean esWebmaster = usuario != null && usuario.getTipo() == TipoEnum.WEBMASTER;
        if (esWebmaster) {
            // Agrupar rutas por repartidor
            var rutas = historialRutaService.obtenerHistorialWebmaster();
            logger.info("Historial: rutas totales={} (webmaster)", rutas == null ? 0 : rutas.size());
            Map<String, List<Ruta>> rutasPorRepartidor = rutas.stream()
                    .filter(r -> r.getUsuario() != null)
                    .collect(Collectors.groupingBy(r -> r.getUsuario().getNombre() + " " + r.getUsuario().getApellidos()));
            model.addAttribute("rutasPorRepartidor", rutasPorRepartidor);
        } else if (usuario != null) {
            var rutas = historialRutaService.obtenerHistorialRepartidor(usuario);
            logger.info("Historial: rutas del usuario {}: {}", usuario.getCorreo(), rutas == null ? 0 : rutas.size());
            model.addAttribute("rutasUsuario", rutas);
        }
        model.addAttribute("esWebmaster", esWebmaster);
        return "historial";
    }

    @GetMapping
    public String mostrarRuta(@RequestParam("repartidorId") Long repartidorId, Model model) {
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
        model.addAttribute("repartidorId", repartidorId);
        return "ruta";
    }

    @GetMapping("/finalizar")
    public String mostrarResumenGet(@RequestParam("repartidorId") Long repartidorId, Model model) {
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
        return "resumenRuta";
    }

    @GetMapping("/resumen")
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
        return "resumenRuta";
    }
}
