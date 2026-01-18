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
import com.paqueteria.utils.generadorCadenas;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.ArrayList;

import com.paqueteria.dto.CrearEnvioDTO;
import com.paqueteria.model.DistanciaEnum;
import com.paqueteria.model.TarifaDistancia;
import com.paqueteria.model.TarifaRangoPeso;
import com.paqueteria.model.Usuario;
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
                tarifaRangoPeso
        );

        envio.setFragil(dto.getFragil());
        if (dto.getNota() != null) {
            envio.setNota(dto.getNota());
        }

        Envio envioGuardado = envioRepository.save(envio);
        return modelMapper.map(envioGuardado, EnvioDTO.class);
    }
}
