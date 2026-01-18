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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
    public UsuarioData findById(Integer id) {
        Usuario usuarioBD = usuarioRepository.findById(id).orElse(null);
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

    @Transactional(readOnly = true)
    public Page<UsuarioData> getTiendasPaginadas(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombreTienda").ascending());
        Page<Usuario> tiendas = usuarioRepository.findByTipo(TipoEnum.CLIENTE, pageable);
        return tiendas.map(tienda -> {
            UsuarioData data = modelMapper.map(tienda, UsuarioData.class);
            // Calcular y asignar el número de envíos de esta tienda
            long numEnvios = envioRepository.countByUsuarioId(tienda.getId());
            data.setNumeroEnvios((int) numEnvios);
            return data;
        });
    }

    @Transactional
    public void eliminarTienda(Integer tiendaId) {
        Usuario tienda = usuarioRepository.findById(tiendaId)
                .orElseThrow(() -> new UsuarioServiceException("Tienda no encontrada"));

        if (tienda.getTipo() != TipoEnum.CLIENTE) {
            throw new UsuarioServiceException("El usuario no es una tienda");
        }

        // Verificar si tiene envíos asociados
        long enviosCount = envioRepository.countByUsuarioId(tiendaId);
        if (enviosCount > 0) {
            throw new UsuarioServiceException("No se puede eliminar la tienda porque tiene " + enviosCount + " envíos asociados");
        }

        usuarioRepository.delete(tienda);
    }
}
