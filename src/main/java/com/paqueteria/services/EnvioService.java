package com.paqueteria.services;

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
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.paqueteria.dto.CrearEnvioDTO;
import com.paqueteria.dto.EnvioDTO;
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

    public Page<Envio> getEnviosPorEstado(EstadoEnum estado, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fecha").ascending());
        return envioRepository.findByEstado(estado, pageable);
    }

    public void asignarRepartidor(Integer envioId, Integer repartidorId) {
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Envío no encontrado"));

        Usuario repartidor = usuarioRepository.findById(repartidorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Repartidor no encontrado"));

        LocalDate fechaRuta = LocalDate.now(); // Usar la fecha actual para la ruta

        // Verificar si el envío actual es urgente (más de 5 días)
        boolean esEnvioUrgente = envio.getFecha().plusDays(4).isBefore(LocalDate.now());

        // Si el envío NO es urgente, verificar si hay envíos urgentes pendientes
        if (!esEnvioUrgente) {
            // Buscar envíos urgentes en estados PENDIENTE, AUSENTE o RECHAZADO
            boolean hayEnviosUrgentes = envioRepository.existeEnviosUrgentes(LocalDate.now().minusDays(4));

            if (hayEnviosUrgentes) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se pueden asignar pedidos no urgentes mientras existan pedidos urgentes pendientes. Por favor, procese primero los pedidos urgentes."
                );
            }
        }

        // Calcular peso ya asignado al repartidor en esta fecha
        BigDecimal pesoAsignado = envioRepository.calcularPesoAsignadoPorRepartidorYFecha(repartidorId, fechaRuta);
        BigDecimal pesoDisponible = repartidor.getPesoMaximo().subtract(pesoAsignado);

        // Validar que el repartidor puede aceptar este envío
        if (envio.getPeso().compareTo(pesoDisponible) > 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "No se puede asignar. El repartidor solo puede cargar " + pesoDisponible + " kg más hoy. Este envío pesa " + envio.getPeso() + " kg."
            );
        }

        // Buscar o crear ruta para el repartidor en esta fecha
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
