package com.paqueteria.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "api")
public class API {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "El nombre no puede ser nulo")
    @Column(nullable = false)
    private String nombre;
    
    @NotNull(message = "La key no puede ser nula")
    @Column(nullable = false, name = "api_key")
    private String key;
    
    @NotNull(message = "El usuario no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    // Constructors
    public API() {}
    
    public API(String nombre, String key, Usuario usuario) {
        this.nombre = nombre;
        this.key = key;
        this.usuario = usuario;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
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
    
    public Usuario getUsuario() {
        return usuario;
    }

    // Método interno para sincronización bidireccional (usar Usuario.addApi() externamente)
    void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        API api = (API) o;
        if (id != null && api.id != null)
            return id.equals(api.id);
        return nombre.equals(api.nombre) && key.equals(api.key);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nombre, key);
    }
}
