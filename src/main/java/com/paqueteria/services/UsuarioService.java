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
                        rep.getPesoMaximo(),
                        rep.getFechaCreacion().toString(),
                        rep.getEnvios(),
                        rep.getActiva(),
                        rep.getTelefono()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RepartidorDTO> getAllRepartidores() {
        List<Usuario> repartidores = usuarioRepository.findByTipoOrderByActivaDesc(TipoEnum.REPARTIDOR);
        return repartidores.stream()
                .map(rep -> new RepartidorDTO(
                        rep.getId(),
                        rep.getApodo(),
                        rep.getNombre(),
                        rep.getApellidos(),
                        rep.getPesoMaximo(),
                        rep.getFechaCreacion().toString(),
                        rep.getEnvios(),
                        rep.getActiva(),
                        rep.getTelefono()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void crearRepartidor(RepartidorDTO dto) {
        // Sanear apodo: reemplazar espacios por guiones bajos
        String apodoSaneado = dto.getApodo().trim().replace(" ", "_");
        
        // Verificar duplicados por apodo (simulado comprobando correo generado)
        String correoGenerado = apodoSaneado + ".driver@paqueteria.com";
        if (usuarioRepository.findByCorreo(correoGenerado).isPresent()) {
            throw new UsuarioServiceException("El usuario (apodo) ya existe");
        }

        Usuario nuevoRepartidor = new Usuario();
        nuevoRepartidor.setNombre(dto.getNombre());
        nuevoRepartidor.setApellidos(dto.getApellidos() != null ? dto.getApellidos() : "");
        nuevoRepartidor.setApodo(apodoSaneado); // Usar apodo saneado
        nuevoRepartidor.setCorreo(correoGenerado);
        nuevoRepartidor.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        nuevoRepartidor.setTipo(TipoEnum.REPARTIDOR);
        nuevoRepartidor.setActiva(true);
        nuevoRepartidor.setFechaCreacion(LocalDate.now());
        
        // Asignar peso máximo y teléfono si vienen en el DTO
        nuevoRepartidor.setPesoMaximo(dto.getPesoMaximo() != null ? dto.getPesoMaximo() : new BigDecimal("100.00"));
        nuevoRepartidor.setTelefono(dto.getTelefono() != null ? dto.getTelefono() : ""); 
        
        usuarioRepository.save(nuevoRepartidor);
    }
    
    @Transactional
    public void editarRepartidor(RepartidorDTO dto) {
        Usuario repartidor = usuarioRepository.findById(dto.getId())
                .orElseThrow(() -> new UsuarioServiceException("Repartidor no encontrado"));
                
        repartidor.setNombre(dto.getNombre());
        repartidor.setApellidos(dto.getApellidos());
        repartidor.setTelefono(dto.getTelefono());
        repartidor.setPesoMaximo(dto.getPesoMaximo());
        
        // Manejar checkbox inactivo (null si no se marca)
        repartidor.setActiva(dto.getActiva() != null ? dto.getActiva() : false);
        
        // Si viene contraseña, actualizarla
        if (dto.getContrasena() != null && !dto.getContrasena().isEmpty()) {
            repartidor.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        }
        
        usuarioRepository.save(repartidor);
    }

    @Transactional
    public void desactivarRepartidor(Integer id) {
        Usuario repartidor = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioServiceException("Repartidor no encontrado"));
        repartidor.setActiva(false);
        usuarioRepository.save(repartidor);
    }
  
    @Transactional
    public void editUser(UsuarioData usuarioData) {
        Usuario usuarioBD = usuarioRepository.findByCorreo(usuarioData.getCorreo()).orElse(null);
        if (usuarioBD == null) {
            throw new UsuarioServiceException("Usuario no encontrado");
        }

        usuarioBD.setNombre(usuarioData.getNombre());
        usuarioBD.setApellidos(usuarioData.getApellidos());
        usuarioBD.setTelefono(usuarioData.getTelefono());
        usuarioBD.setApodo(usuarioData.getApodo());
        usuarioBD.setNombreTienda(usuarioData.getNombreTienda());
        usuarioBD.setPesoMaximo(usuarioData.getPesoMaximo());
        usuarioBD.setActiva(usuarioData.getActiva());

        usuarioRepository.save(usuarioBD);
    }
}
