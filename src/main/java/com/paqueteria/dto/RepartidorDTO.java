package com.paqueteria.dto;

import java.math.BigDecimal;

public class RepartidorDTO {
    private Integer id;
    private String apodo;
    private String nombre;
    private String apellidos;
    private BigDecimal pesoMaximo;
    private String fechaCreacion;
    private Integer envios;
    private Boolean activa;
    private String contrasena; // Solo para creación/edición
    private String telefono;

    // Constructor vacío
    public RepartidorDTO() {}

    // Constructor con parámetros
    public RepartidorDTO(Integer id, String apodo, String nombre, String apellidos, BigDecimal pesoMaximo, String fechaCreacion, Integer envios, Boolean activa, String telefono) {
        this.id = id;
        this.apodo = apodo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.pesoMaximo = pesoMaximo;
        this.fechaCreacion = fechaCreacion;
        this.envios = envios;
        this.activa = activa;
        this.telefono = telefono;
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

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getEnvios() {
        return envios;
    }

    public void setEnvios(Integer envios) {
        this.envios = envios;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}

