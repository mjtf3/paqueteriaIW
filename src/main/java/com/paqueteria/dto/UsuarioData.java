package com.paqueteria.dto;

import com.paqueteria.model.TipoEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;

import java.util.Objects;


import java.math.BigDecimal;
import java.time.LocalDate;

public class UsuarioData {
    private Integer id;
    private String apodo;
    private String nombre;
    private String nombreTienda;
    private String apellidos;
    @Enumerated(EnumType.STRING)
    private TipoEnum tipo;
    @Email
    private String correo;
    private String telefono;
    private String contrasena;
    private LocalDate fechaCreacion;
    private BigDecimal pesoMaximo;
    private Boolean activa = true;
    private Integer numeroEnvios;

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

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public TipoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoEnum tipo) {
        this.tipo = tipo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getPesoMaximo() {
        return pesoMaximo;
    }

    public void setPesoMaximo(BigDecimal pesoMaximo) {
        this.pesoMaximo = pesoMaximo;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Integer getNumeroEnvios() {
        return numeroEnvios;
    }

    public void setNumeroEnvios(Integer numeroEnvios) {
        this.numeroEnvios = numeroEnvios;
    }

    public Boolean isAdmin() {
        return this.tipo == TipoEnum.WEBMASTER;
    }

    public Boolean isTienda() {
        return this.tipo == TipoEnum.CLIENTE;
    }

    public Boolean isRepartidor() {
        return this.tipo == TipoEnum.REPARTIDOR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioData)) return false;
        UsuarioData that = (UsuarioData) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
