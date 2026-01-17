package com.paqueteria.services;

import com.paqueteria.dto.UsuarioData;
import com.paqueteria.model.Usuario;
import com.paqueteria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


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
    public UsuarioData actualizarDatos(String correo, UsuarioData usuarioData) {
        Usuario usuarioBD = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new UsuarioServiceException("Usuario no encontrado"));
        
        // Update only editable fields
        if (usuarioData.getNombre() != null && !usuarioData.getNombre().isEmpty()) {
            usuarioBD.setNombre(usuarioData.getNombre());
        }
        if (usuarioData.getApellidos() != null && !usuarioData.getApellidos().isEmpty()) {
            usuarioBD.setApellidos(usuarioData.getApellidos());
        }
        if (usuarioData.getTelefono() != null && !usuarioData.getTelefono().isEmpty()) {
            usuarioBD.setTelefono(usuarioData.getTelefono());
        }
        if (usuarioData.getApodo() != null && !usuarioData.getApodo().isEmpty()) {
            usuarioBD.setApodo(usuarioData.getApodo());
        }
        if (usuarioData.getNombreTienda() != null && !usuarioData.getNombreTienda().isEmpty()) {
            usuarioBD.setNombreTienda(usuarioData.getNombreTienda());
        }
        
        usuarioRepository.save(usuarioBD);
        return modelMapper.map(usuarioBD, UsuarioData.class);
    }
}
