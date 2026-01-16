package com.paqueteria.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paqueteria.dto.CrearEnvioDTO;
import com.paqueteria.dto.EnvioDTO;
import com.paqueteria.model.DistanciaEnum;
import com.paqueteria.model.Envio;
import com.paqueteria.model.TarifaDistancia;
import com.paqueteria.model.TarifaRangoPeso;
import com.paqueteria.model.Usuario;
import com.paqueteria.repository.EnvioRepository;
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

    public EnvioDTO crearEnvio(CrearEnvioDTO dto, Integer usuarioId) {
        // Obtener usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        // Obtener tarifas
        DistanciaEnum distancia = DistanciaEnum.values()[dto.getDistancia() - 1];
        TarifaDistancia tarifaDistancia = tarifaDistanciaRepository.findByDistanciaAndActiva(distancia)
                .orElseThrow(() -> new RuntimeException("Tarifa de distancia no encontrada"));

        Integer pesoEntero = dto.getPeso().intValue();
        TarifaRangoPeso tarifaRangoPeso = tarifaRangoPesoRepository.findByPesoAndActiva(pesoEntero)
                .orElseThrow(() -> new RuntimeException("Tarifa de peso no encontrada"));

        // Calcular coste total
        BigDecimal costeTotal = tarifaDistancia.getCoste().add(tarifaRangoPeso.getCoste());

        // Generar localizador único
        String localizador = generarLocalizador();

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

    private String generarLocalizador() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
