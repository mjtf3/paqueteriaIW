package com.paqueteria.dto;

import java.math.BigDecimal;

public class RepartidorDTO {
    private Integer id;
    private String apodo;
    private String nombre;
    private String apellidos;
    private BigDecimal pesoMaximo;

    // Constructor vacío
    public RepartidorDTO() {}

    // Constructor con parámetros
    public RepartidorDTO(Integer id, String apodo, String nombre, String apellidos, BigDecimal pesoMaximo) {
        this.id = id;
        this.apodo = apodo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.pesoMaximo = pesoMaximo;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public BigDecimal getPesoMaximo() {
        return pesoMaximo;
    }

    public void setPesoMaximo(BigDecimal pesoMaximo) {
        this.pesoMaximo = pesoMaximo;
    }
}

