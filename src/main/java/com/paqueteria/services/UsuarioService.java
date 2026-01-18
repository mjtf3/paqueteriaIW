package com.paqueteria.services;

import com.paqueteria.dto.ApiData;
import com.paqueteria.dto.UsuarioData;
import com.paqueteria.model.Usuario;
import com.paqueteria.model.API;
import com.paqueteria.model.TipoEnum;
import com.paqueteria.repository.UsuarioRepository;
import com.paqueteria.repository.EnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnvioRepository envioRepository;

    @Transactional
    public UsuarioData registrar(UsuarioData usuario) {
        Optional<Usuario> usuarioBD = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (usuarioBD.isPresent()) {
            throw new UsuarioServiceException("El usuario " + usuario.getCorreo() + " ya existe");
        }
        else if(usuario.getCorreo() == null){
            throw new UsuarioServiceException("El correo no puede ser nulo");
        } else if (usuario.getContrasena() == null) {
            throw new UsuarioServiceException("La contrasena no puede ser nula");
        }
        else {
            Usuario usuarioNuevo = modelMapper.map(usuario,Usuario.class);
            usuarioNuevo.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            usuarioRepository.save(usuarioNuevo);

            return modelMapper.map(usuarioNuevo,UsuarioData.class);
        }
    }

    @Transactional
    public UsuarioData findByCorreo(String correo) {
        Usuario usuarioBD = usuarioRepository.findByCorreo(correo).orElse(null);
        if (usuarioBD == null) {return null;}
        return modelMapper.map(usuarioBD,UsuarioData.class);
    }

    @Transactional
    public void addApi(UsuarioData usuario, ApiData apiKey) {
        Usuario usuarioBD = usuarioRepository.findByCorreo(usuario.getCorreo()).orElse(null);
        if (usuarioBD == null) {
            return;
        }
        API api = modelMapper.map(apiKey,API.class);
        api.setKey(passwordEncoder.encode(apiKey.getKey()));
        usuarioBD.addApi(api);
        usuarioRepository.save(usuarioBD);
    }

    @Transactional
    public List<ApiData> getAPIs(UsuarioData usuario) {
        Usuario usuarioBD = usuarioRepository.findByCorreo(usuario.getCorreo()).orElse(null);
        if (usuarioBD == null) {
            return null;
        }
        return usuarioBD.getApis().stream().map(api -> modelMapper.map(api,ApiData.class)).toList();
    }

    @Transactional
    public void removeApi(UsuarioData usuario, ApiData apiKey) {
        Usuario usuarioBD = usuarioRepository.findByCorreo(usuario.getCorreo()).orElse(null);
        if (usuarioBD == null) {
            return;
        }
        API api = modelMapper.map(apiKey,API.class);
        usuarioBD.removeApi(api);
        usuarioRepository.save(usuarioBD);
    }

    @Transactional(readOnly = true)
    public List<Usuario> getRepartidoresActivos() {
        return usuarioRepository.findByTipoAndActivaTrue(TipoEnum.REPARTIDOR);
    }

    @Transactional(readOnly = true)
    public List<Usuario> getRepartidoresDisponiblesParaFecha(LocalDate fecha) {
        List<Usuario> repartidores = usuarioRepository.findByTipoAndActivaTrue(TipoEnum.REPARTIDOR);

        // Filtrar solo los que no han superado su peso máximo
        return repartidores.stream()
                .filter(rep -> {
                    BigDecimal pesoAsignado = envioRepository.calcularPesoAsignadoPorRepartidorYFecha(rep.getId(), fecha);
                    return pesoAsignado.compareTo(rep.getPesoMaximo()) < 0;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal getPesoDisponibleRepartidor(Integer repartidorId, LocalDate fecha) {
        Usuario repartidor = usuarioRepository.findById(repartidorId)
                .orElseThrow(() -> new IllegalArgumentException("Repartidor no encontrado"));

        BigDecimal pesoAsignado = envioRepository.calcularPesoAsignadoPorRepartidorYFecha(repartidorId, fecha);
        return repartidor.getPesoMaximo().subtract(pesoAsignado);
    }
}
