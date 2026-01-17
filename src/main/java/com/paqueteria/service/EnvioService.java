package com.paqueteria.service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paqueteria.dto.EnvioDTO;
import com.paqueteria.model.Envio;
import com.paqueteria.model.EstadoEnum;
import com.paqueteria.repository.EnvioRepository;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

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
        switch (estado) {
            case PENDIENTE:
                return "EN ALMACEN";
            case RUTA:
                return "EN REPARTO";
            case ENTREGADO:
                return "ENTREGADO";
            case AUSENTE:
                return "AUSENTE";
            case RECHAZADO:
                return "RECHAZADO";
            default:
                return "EN_ALMACEN";
        }
    }
}
