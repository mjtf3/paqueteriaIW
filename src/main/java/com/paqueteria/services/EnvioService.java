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

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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
