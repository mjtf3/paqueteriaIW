package com.paqueteria.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tarifas_rango_peso")
public class TarifaRangoPeso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "peso_minimo", nullable = false)
    private Integer pesoMinimo;

    @Column(name = "peso_maximo", nullable = false)
    private Integer pesoMaximo;

    @Column(nullable = false)
    private Float coste;

    @Column(nullable = false)
    private Boolean activa;

    public TarifaRangoPeso() {
    }

    public TarifaRangoPeso(Integer pesoMinimo, Integer pesoMaximo, Float coste, Boolean activa) {
        this.pesoMinimo = pesoMinimo;
        this.pesoMaximo = pesoMaximo;
        this.coste = coste;
        this.activa = activa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}

