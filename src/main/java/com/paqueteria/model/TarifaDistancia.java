package com.paqueteria.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tarifas_distancia")
public class TarifaDistancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private DistanciaEnum distancia;

    @Column(nullable = false)
    private Float coste;

    @Column(nullable = false)
    private Boolean activa;

    public TarifaDistancia() {
    }

    public TarifaDistancia(DistanciaEnum distancia, Float coste, Boolean activa) {
        this.distancia = distancia;
        this.coste = coste;
        this.activa = activa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DistanciaEnum getDistancia() {
        return distancia;
    }

    public void setDistancia(DistanciaEnum distancia) {
        this.distancia = distancia;
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
}