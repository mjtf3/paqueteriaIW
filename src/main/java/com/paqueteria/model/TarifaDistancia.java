package com.paqueteria.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tarifa_distancia")
public class TarifaDistancia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "La distancia no puede ser nula")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private DistanciaEnum distancia;
    
    @NotNull(message = "El coste no puede ser nulo")
    @Column(nullable = false)
    private BigDecimal coste;
    
    @NotNull(message = "El estado activa no puede ser nulo")
    @Column(nullable = false)
    private Boolean activa = true;
    
    @OneToMany(mappedBy = "tarifaDistancia")
    private final List<Envio> envios = new ArrayList<>();
    
    // Constructors
    public TarifaDistancia() {}
    
    public TarifaDistancia(DistanciaEnum distancia, BigDecimal coste) {
        this.distancia = distancia;
        this.coste = coste;
        this.activa = true;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public DistanciaEnum getDistancia() {
        return distancia;
    }
    
    public void setDistancia(DistanciaEnum distancia) {
        this.distancia = distancia;
    }
    
    public BigDecimal getCoste() {
        return coste;
    }
    
    public void setCoste(BigDecimal coste) {
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
        envio.setTarifaDistancia(this);
    }
    
    public void removeEnvio(Envio envio) {
        if (envio == null || !this.envios.contains(envio)) return;
        this.envios.remove(envio);
        envio.setTarifaDistancia(null);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TarifaDistancia that = (TarifaDistancia) o;
        if (id != null && that.id != null)
            return id.equals(that.id);
        return distancia == that.distancia;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(distancia);
    }
}
