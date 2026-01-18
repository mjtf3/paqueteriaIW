package com.paqueteria.services;

import com.paqueteria.dto.ApiData;
import com.paqueteria.model.API;
import com.paqueteria.model.Usuario;
import com.paqueteria.repository.APIRepository;
import com.paqueteria.utils.generadorCadenas;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ApiService {

    @Autowired
    APIRepository apiRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ApiData findById(Integer id) {
        API apiBD = apiRepository.findById(id).orElse(null);
        if (apiBD == null) {
            return null;
        }
        return modelMapper.map(apiBD, ApiData.class);
    }

    public Usuario validateAndGetUser(String apiKey) {
        String apiKeyHash = generadorCadenas.hashSHA256(apiKey);
        API api = apiRepository
            .findByKey(apiKeyHash)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "API Key inválida"));

        Usuario usuario = api.getUsuario();
        if (usuario == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Usuario no asociado a la API Key, contacte al administrador"
            );
        }
        return usuario;
    }
}
