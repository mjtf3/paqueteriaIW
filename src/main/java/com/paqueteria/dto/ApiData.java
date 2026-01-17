package com.paqueteria.dto;

import java.time.LocalDate;

public class ApiData {
    Integer id;
    String nombre;
    String key;
    LocalDate fecha;
    UsuarioData usuarioData;

    public ApiData() {}

    public ApiData(Integer id, String nombre, String key, UsuarioData usuarioData, LocalDate fecha) {
        this.id = id;
        this.nombre = nombre;
        this.key = key;
        this.usuarioData = usuarioData;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
