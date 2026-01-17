package com.paqueteria.security;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class ApiKeyAspect {

    @Value("${api.key}")
    private String validApiKey;

    @Before("@annotation(RequireApiKey)")
    public void validateApiKey() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No se pudo acceder al contexto de la petición");
        }

        HttpServletRequest request = attributes.getRequest();
        String apiKey = request.getHeader("X-API-Key");

        if (apiKey == null || apiKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "API Key no proporcionada");
        }

        if (!apiKey.equals(validApiKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "API Key inválida");
        }
    }
}
