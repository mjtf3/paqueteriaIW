package com.paqueteria.services;

import com.paqueteria.dto.ApiData;
import com.paqueteria.dto.RepartidorDTO;
import com.paqueteria.dto.UsuarioData;
import com.paqueteria.model.Usuario;
import com.paqueteria.model.API;
import com.paqueteria.model.TipoEnum;
import com.paqueteria.repository.UsuarioRepository;
import com.paqueteria.repository.EnvioRepository;
import com.paqueteria.utils.generadorCadenas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.ArrayList;
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
        api.setKey(generadorCadenas.hashSHA256(apiKey.getKey()));
        usuarioBD.addApi(api);
        usuarioRepository.save(usuarioBD);
    }

    @Transactional
    public List<ApiData> getAPIs(UsuarioData usuario) {
        Usuario usuarioBD = usuarioRepository.findByCorreo(usuario.getCorreo()).orElse(null);
        if (usuarioBD == null) {
            System.out.println("Usuario no encontrado");
            return new ArrayList<>();
        }
        List<API> apis = usuarioBD.getApis();
        if (apis == null) {
            System.out.println("No APIs");
            return new ArrayList<>();
        }

        List<ApiData> resultado = new ArrayList<>();
        for (API api : apis) {
            resultado.add(modelMapper.map(api, ApiData.class));
        }
        return resultado;
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
    public List<RepartidorDTO> getRepartidoresActivos() {
        List<Usuario> repartidores = usuarioRepository.findByTipoAndActivaTrue(TipoEnum.REPARTIDOR);
        return repartidores.stream()
                .map(rep -> new RepartidorDTO(
                        rep.getId(),
                        rep.getApodo(),
                        rep.getNombre(),
                        rep.getApellidos(),
                        rep.getPesoMaximo()
                ))
                .collect(Collectors.toList());
    }
}
