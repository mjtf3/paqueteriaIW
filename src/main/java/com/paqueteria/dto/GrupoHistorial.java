package com.paqueteria.dto;

import com.paqueteria.model.Ruta;
import java.util.List;

public record GrupoHistorial(String titulo, List<Ruta> rutas) {}