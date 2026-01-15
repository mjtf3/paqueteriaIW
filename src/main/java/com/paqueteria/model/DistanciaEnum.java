package com.paqueteria.model;

public enum DistanciaEnum {
    CIUDAD("Ciudad"),
    PROVINCIAL("Provincial"),
    NACIONAL("Nacional"),
    INTERNACIONAL("Internacional");

    private final String nombre;

    DistanciaEnum(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}

