package com.paqueteria.services;

import com.paqueteria.dto.ApiData;
import com.paqueteria.model.API;
import com.paqueteria.repository.APIRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    @Autowired
    APIRepository apiRepository;
    @Autowired
    private ModelMapper modelMapper;

    public ApiData findById(Integer id){
        API apiBD = apiRepository.findById(id).orElse(null);
        return modelMapper.map(apiBD, ApiData.class);
    }
}
