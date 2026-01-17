package com.paqueteria.services;

import com.paqueteria.dto.ApiData;
import com.paqueteria.dto.UsuarioData;
import com.paqueteria.model.Usuario;
import com.paqueteria.model.API;
import com.paqueteria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

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
}
