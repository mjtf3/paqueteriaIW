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
        String label = mapEstadoToLabel(envio.getEstado());

        EnvioDTO envioDTO = new EnvioDTO(
            envio.getLocalizador(),
            frontendStatus,
            label,
            envio.getDireccionOrigen(),
            envio.getDireccionDestino(),
            envio.getFecha().format(DATE_FORMATTER)
        );

        return Optional.of(envioDTO);
    }

    private String mapEstadoToFrontendStatus(EstadoEnum estado) {
        switch (estado) {
            case PENDIENTE:
                return "EN_ALMACEN";
            case RUTA:
                return "EN_REPARTO";
            case ENTREGADO:
                return "ENTREGADO";
            case AUSENTE:
            case RECHAZADO:
                return "EN_REPARTO";
            default:
                return "EN_ALMACEN";
        }
    }

    public String mapEstadoToLabel(EstadoEnum estado) {
        switch (estado) {
            case PENDIENTE:
                return "Pendiente";
            case RUTA:
                return "En ruta";
            case ENTREGADO:
                return "Entregado";
            case AUSENTE:
                return "Ausente";
            case RECHAZADO:
                return "Rechazado";
            default:
                return "Pendiente";
        }
    }
}
