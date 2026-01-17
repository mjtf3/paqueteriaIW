package com.paqueteria.utils;

import java.util.UUID;

public class generadorCadenas {
    static public String generarCadena() {
        return UUID.randomUUID().toString().toUpperCase();
    }
}
