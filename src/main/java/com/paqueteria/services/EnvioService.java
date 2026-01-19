package com.paqueteria.services;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paqueteria.dto.EnvioDTO;
import com.paqueteria.model.Envio;
import com.paqueteria.model.EstadoEnum;
import com.paqueteria.repository.EnvioRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.paqueteria.utils.generadorCadenas;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.ArrayList;

import com.paqueteria.dto.CrearEnvioDTO;
import com.paqueteria.model.DistanciaEnum;
import com.paqueteria.model.Envio;
import com.paqueteria.model.EstadoEnum;
import com.paqueteria.model.Ruta;
import com.paqueteria.model.TarifaDistancia;
import com.paqueteria.model.TarifaRangoPeso;
import com.paqueteria.model.Usuario;
import com.paqueteria.repository.EnvioRepository;
import com.paqueteria.repository.RutaRepository;
import com.paqueteria.repository.TarifaDistanciaRepository;
import com.paqueteria.repository.TarifaRangoPesoRepository;
import com.paqueteria.repository.UsuarioRepository;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TarifaDistanciaRepository tarifaDistanciaRepository;

    @Autowired
    private TarifaRangoPesoRepository tarifaRangoPesoRepository;

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<EnvioDTO> obtenerEnviosPorRepartidorTodosEstados(Long repartidorId) {
        List<Envio> envios = envioRepository.findAll();
        List<EnvioDTO> resultado = new ArrayList<>();
        for (Envio envio : envios) {
            if (envio.getUsuario() != null && envio.getUsuario().getId().equals(repartidorId.intValue())) {
                resultado.add(new EnvioDTO(envio));
            }
        }
        return resultado;
    }

    public int contarPaquetesPendientesRepartidor(Integer repartidorId) {
        return (int) envioRepository.findAll().stream()
                .filter(e -> e.getUsuario().getId().equals(repartidorId))
                .filter(e -> e.getEstado() == EstadoEnum.RUTA)
                .count();
    }

    public List<EnvioDTO> obtenerEnviosPorRepartidor(Long repartidorId) {
        List<Envio> envios = envioRepository.findAll();
        List<EnvioDTO> resultado = new ArrayList<>();
        for (Envio envio : envios) {
            if (envio.getUsuario() != null && envio.getUsuario().getId().equals(repartidorId.intValue()) &&
                (envio.getEstado() == EstadoEnum.PENDIENTE || envio.getEstado() == EstadoEnum.RUTA)) {
                resultado.add(new EnvioDTO(envio));
            }
        }
        return resultado;
    }
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        public void cambiarEstadoEnvio(Integer envioId, String estado) {
            Optional<Envio> envioOpt = envioRepository.findById(envioId);
            if (envioOpt.isPresent()) {
                Envio envio = envioOpt.get();
                EstadoEnum nuevoEstado;
                switch (estado.toUpperCase()) {
                    case "RECHAZADO":
                        nuevoEstado = EstadoEnum.RECHAZADO;
                        break;
                    case "AUSENTE":
                        nuevoEstado = EstadoEnum.AUSENTE;
                        break;
                    case "ENTREGADO":
                        nuevoEstado = EstadoEnum.ENTREGADO;
                        break;
                    default:
                        throw new IllegalArgumentException("Estado no válido");
                }
                envio.setEstado(nuevoEstado);
                envioRepository.save(envio);
            } else {
                throw new IllegalArgumentException("Envío no encontrado");
            }
        }

    /**
     * Asigna la ruta indicada a todos los envíos del repartidor que estén en estado
     * PENDIENTE o RUTA. Esto evita que envíos ya ENTREGADOS de rutas pasadas se
     * contabilicen en la nueva ruta.
     */
    public void asignarRutaAEnviosActivosDelRepartidor(Integer repartidorId, com.paqueteria.model.Ruta ruta) {
        List<Envio> envios = envioRepository.findAll();
        for (Envio envio : envios) {
            if (envio.getUsuario() != null && envio.getUsuario().getId().equals(repartidorId)) {
                if (envio.getEstado() == EstadoEnum.PENDIENTE || envio.getEstado() == EstadoEnum.RUTA) {
                    envio.setRuta(ruta);
                    envioRepository.save(envio);
                }
            }
        }
    }

    public Optional<EnvioDTO> getTrackingInfo(String localizador) {
        Optional<Envio> envioOpt = envioRepository.findByLocalizador(localizador);

        if (envioOpt.isEmpty()) {
            return Optional.empty();
        }

        Envio envio = envioOpt.get();

        String frontendStatus = mapEstadoToFrontendStatus(envio.getEstado());

        EnvioDTO envioDTO = new EnvioDTO(
            envio.getLocalizador(),
            frontendStatus,
            envio.getDireccionOrigen(),
            envio.getDireccionDestino(),
            envio.getFecha().format(DATE_FORMATTER)
        );

        return Optional.of(envioDTO);
    }

    public String mapEstadoToFrontendStatus(EstadoEnum estado) {
        return (estado != null) ? estado.getDisplayName() : "EN ALMACEN";
    }

    


    public EnvioDTO crearEnvio(CrearEnvioDTO dto, Integer usuarioId) {
        // Obtener usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + usuarioId));

        // Obtener tarifas
        DistanciaEnum distancia = dto.getDistancia();
        TarifaDistancia tarifaDistancia = tarifaDistanciaRepository.findByDistanciaAndActiva(distancia, true)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarifa de distancia no encontrada"));

        Integer pesoEntero = dto.getPeso().intValue();
        TarifaRangoPeso tarifaRangoPeso = tarifaRangoPesoRepository.findByPesoAndActiva(pesoEntero)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarifa de peso no encontrada"));

        // Calcular coste total
        BigDecimal costeTotal = tarifaDistancia.getCoste().add(tarifaRangoPeso.getCoste());

        // Generar localizador único
        String localizador = generadorCadenas.generarCadena();

        // Crear envío
        Envio envio = new Envio(
                localizador,
                dto.getDireccionOrigen(),
                dto.getDireccionDestino(),
                dto.getNombreComprador(),
                dto.getPeso(),
                distancia,
                dto.getNumeroPaquetes(),
                costeTotal,
                usuario,
                tarifaDistancia,
                tarifaRangoPeso,
                dto.getFecha()
        );

        envio.setFragil(dto.getFragil());
        if (dto.getNota() != null) {
            envio.setNota(dto.getNota());
        }

        Envio envioGuardado = envioRepository.save(envio);
        return modelMapper.map(envioGuardado, EnvioDTO.class);
    }

    public List<EnvioDTO> obtenerEnviosPorRuta(Integer rutaId) {
        List<Envio> envios = envioRepository.findAll();
        List<EnvioDTO> resultado = new ArrayList<>();
        for (Envio envio : envios) {
            if (envio.getRuta() != null && envio.getRuta().getId() != null && envio.getRuta().getId().equals(rutaId)) {
                resultado.add(new EnvioDTO(envio));
            }
        }
        return resultado;
    }

    public List<EnvioDTO> obtenerEnviosPorFecha(LocalDate fecha) {
        List<Envio> envios = envioRepository.findAll();
        List<EnvioDTO> resultado = new ArrayList<>();
        if (fecha == null) return resultado;
        for (Envio envio : envios) {
            if (envio.getFecha() != null && fecha.equals(envio.getFecha())) {
                resultado.add(new EnvioDTO(envio));
            }
        }
        return resultado;
    }
  
    public Page<EnvioDTO> getEnviosPorEstado(EstadoEnum estado, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fecha").ascending());
        Page<Envio> envios = envioRepository.findByEstado(estado, pageable);

        LocalDate fechaActual = LocalDate.now();

        return envios.map(envio -> {
            EnvioDTO dto = modelMapper.map(envio, EnvioDTO.class);
            // Calcular si es urgente (más de 4 días desde la fecha del envío)
            if (envio.getFecha() != null) {
                LocalDate fechaLimite = envio.getFecha().plusDays(4);
                dto.setEsUrgente(fechaLimite.isBefore(fechaActual));
            } else {
                dto.setEsUrgente(false);
            }
            return dto;
        });
    }

    public void asignarRepartidor(Integer envioId, Integer repartidorId) {
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Envío no encontrado"));

        Usuario repartidor = usuarioRepository.findById(repartidorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Repartidor no encontrado"));

        LocalDate fechaRuta = LocalDate.now(); // Usar la fecha actual para la ruta


        boolean esEnvioUrgente = envio.getFecha().plusDays(4).isBefore(LocalDate.now());

        if (!esEnvioUrgente) {
            boolean hayEnviosUrgentes = envioRepository.existeEnviosUrgentes(LocalDate.now().minusDays(4));

            if (hayEnviosUrgentes) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se pueden asignar pedidos no urgentes mientras existan pedidos urgentes pendientes. Por favor, procese primero los pedidos urgentes."
                );
            }
        }

        BigDecimal pesoAsignado = envioRepository.calcularPesoAsignadoPorRepartidorYFecha(repartidorId, fechaRuta);
        BigDecimal pesoDisponible = repartidor.getPesoMaximo().subtract(pesoAsignado);

        if (envio.getPeso().compareTo(pesoDisponible) > 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "No se puede asignar. El repartidor solo puede cargar " + pesoDisponible + " kg más hoy. Este envío pesa " + envio.getPeso() + " kg."
            );
        }

        Ruta ruta = rutaRepository.findByUsuarioAndFecha(repartidor, fechaRuta)
                .orElseGet(() -> {
                    Ruta nuevaRuta = new Ruta(fechaRuta, repartidor);
                    return rutaRepository.save(nuevaRuta);
                });

        envio.setRuta(ruta);
        envio.setEstado(EstadoEnum.RUTA);
        envioRepository.save(envio);
    }
}
