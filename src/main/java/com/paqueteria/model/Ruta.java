package com.paqueteria.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ruta")
public class Ruta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "La fecha no puede ser nula")
    @Column(nullable = false)
    private LocalDate fecha;
    
    @NotNull(message = "El usuario no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    // Los envios se añaden uno a uno
    @OneToMany(mappedBy = "ruta")
    private final List<Envio> envios = new ArrayList<>();
    
    // Constructors
    public Ruta() {}
    
    public Ruta(LocalDate fecha, Usuario usuario) {
        this.fecha = fecha;
        this.usuario = usuario;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // revisar estos metodos 
    public List<Envio> getEnvios() {
        return envios;
    }
    
    // Helper methods para manejar relación bidireccional
    public void addEnvio(Envio envio) {
        if (this.envios.contains(envio)) return;
        this.envios.add(envio);
        envio.setRuta(this);
    }
    
    public void removeEnvio(Envio envio) {
        if (envio == null || !this.envios.contains(envio)) return;
        this.envios.remove(envio);
        envio.setRuta(null);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ruta ruta = (Ruta) o;
        if (id != null && ruta.id != null)
            return id.equals(ruta.id);
        return fecha.equals(ruta.fecha) && usuario.equals(ruta.usuario);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(fecha, usuario);
    }
}
