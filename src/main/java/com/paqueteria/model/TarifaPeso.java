package com.paqueteria.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tarifas_peso")
public class TarifaPeso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer pesoMin;
    private Integer pesoMax;
    private Float precio;
    private String descripcion;

    public TarifaPeso() {
    }

    public TarifaPeso(Integer pesoMin, Integer pesoMax, Float precio, String descripcion) {
        this.pesoMin = pesoMin;
        this.pesoMax = pesoMax;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPesoMin() {
        return pesoMin;
    }

    public void setPesoMin(Integer pesoMin) {
        this.pesoMin = pesoMin;
    }

    public Integer getPesoMax() {
        return pesoMax;
    }

    public void setPesoMax(Integer pesoMax) {
        this.pesoMax = pesoMax;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

