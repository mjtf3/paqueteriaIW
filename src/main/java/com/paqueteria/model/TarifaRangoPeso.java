package com.paqueteria.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tarifa_rango_peso")
public class TarifaRangoPeso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "El peso mínimo no puede ser nulo")
    @Column(name = "peso_minimo", nullable = false)
    private Integer pesoMinimo;
    
    @NotNull(message = "El peso máximo no puede ser nulo")
    @Column(name = "peso_maximo", nullable = false)
    private Integer pesoMaximo;
    
    @NotNull(message = "El coste no puede ser nulo")
    @Column(nullable = false)
    private Float coste;
    
    @NotNull(message = "El estado 'activa' no puede ser nulo")
    @Column(nullable = false)
    private Boolean activa = true;
    
    @OneToMany(mappedBy = "tarifaRangoPeso")
    private final List<Envio> envios = new ArrayList<>();
    
    // Constructors
    public TarifaRangoPeso() {}
    
    public TarifaRangoPeso(Integer pesoMinimo, Integer pesoMaximo, Float coste) {
        this.pesoMinimo = pesoMinimo;
        this.pesoMaximo = pesoMaximo;
        this.coste = coste;
        this.activa = true;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public Integer getPesoMinimo() {
        return pesoMinimo;
    }
    
    public void setPesoMinimo(Integer pesoMinimo) {
        this.pesoMinimo = pesoMinimo;
    }
    
    public Integer getPesoMaximo() {
        return pesoMaximo;
    }
    
    public void setPesoMaximo(Integer pesoMaximo) {
        this.pesoMaximo = pesoMaximo;
    }
    
    public Float getCoste() {
        return coste;
    }
    
    public void setCoste(Float coste) {
        this.coste = coste;
    }
    
    public Boolean getActiva() {
        return activa;
    }
    
    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
    
    public List<Envio> getEnvios() {
        return envios;
    }
    
    // Helper methods para manejar relación bidireccional
    public void addEnvio(Envio envio) {
        if (this.envios.contains(envio)) return;
        this.envios.add(envio);
        envio.setTarifaRangoPeso(this);
    }
    
    public void removeEnvio(Envio envio) {
        if (envio == null || !this.envios.contains(envio)) return;
        this.envios.remove(envio);
        envio.setTarifaRangoPeso(null);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TarifaRangoPeso that = (TarifaRangoPeso) o;
        if (id != null && that.id != null)
            return id.equals(that.id);
        return pesoMinimo.equals(that.pesoMinimo) && pesoMaximo.equals(that.pesoMaximo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(pesoMinimo, pesoMaximo);
    }
}
