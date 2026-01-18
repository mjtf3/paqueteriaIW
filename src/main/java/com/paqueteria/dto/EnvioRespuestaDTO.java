package com.paqueteria.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class EnvioRespuestaDTO {

    private String localizador;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaEstimada;

    public EnvioRespuestaDTO() {}

    public EnvioRespuestaDTO(String localizador, LocalDate fechaEstimada) {
        this.localizador = localizador;
        this.fechaEstimada = agregarDiasLaborables(fechaEstimada, 5);
    }

    public String getLocalizador() {
        return localizador;
    }

    public void setLocalizador(String localizador) {
        this.localizador = localizador;
    }

    public LocalDate getFechaEstimada() {
        return fechaEstimada;
    }

    public void setFechaEstimada(LocalDate fechaEstimada) {
        this.fechaEstimada = agregarDiasLaborables(fechaEstimada, 5);
    }

    private LocalDate agregarDiasLaborables(LocalDate fecha, int diasLaborables) {
        if (fecha == null) {
            return null;
        }
        LocalDate resultado = fecha;
        int diasAgregados = 0;
        while (diasAgregados < diasLaborables) {
            resultado = resultado.plusDays(1);
            if (resultado.getDayOfWeek() != DayOfWeek.SATURDAY && resultado.getDayOfWeek() != DayOfWeek.SUNDAY) {
                diasAgregados++;
            }
        }
        return resultado;
    }
}
