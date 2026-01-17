package com.paqueteria.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.paqueteria.model.DistanciaEnum;
import com.paqueteria.model.Envio;
import com.paqueteria.model.EstadoEnum;

public class EnvioDTO {

    private Integer id;
    private String localizador;
    private String direccionOrigen;
    private String direccionDestino;
    private EstadoEnum estado;
    private String nombreComprador;
    private String nota;
    private BigDecimal peso;
    private DistanciaEnum distancia;
    private Boolean fragil;
    private Integer numeroPaquetes;
    private BigDecimal costeTotal;
    private LocalDate fecha;
    private String nombreUsuario;
    private Integer usuarioId;

    // Constructor vacío
    public EnvioDTO() {
    }

    // Constructor desde entidad
    public EnvioDTO(Envio envio) {
        this.id = envio.getId();
        this.localizador = envio.getLocalizador();
        this.direccionOrigen = envio.getDireccionOrigen();
        this.direccionDestino = envio.getDireccionDestino();
        this.estado = envio.getEstado();
        this.nombreComprador = envio.getNombreComprador();
        this.nota = envio.getNota();
        this.peso = envio.getPeso();
        this.distancia = envio.getDistancia();
        this.fragil = envio.getFragil();
        this.numeroPaquetes = envio.getNumeroPaquetes();
        this.costeTotal = envio.getCosteTotal();
        this.fecha = envio.getFecha();
        this.usuarioId = envio.getUsuario().getId();
        this.nombreUsuario = envio.getUsuario().getNombre();
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocalizador() {
        return localizador;
    }

    public void setLocalizador(String localizador) {
        this.localizador = localizador;
    }

    public String getDireccionOrigen() {
        return direccionOrigen;
    }

    public void setDireccionOrigen(String direccionOrigen) {
        this.direccionOrigen = direccionOrigen;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public EstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public DistanciaEnum getDistancia() {
        return distancia;
    }

    public void setDistancia(DistanciaEnum distancia) {
        this.distancia = distancia;
    }

    public Boolean getFragil() {
        return fragil;
    }

    public void setFragil(Boolean fragil) {
        this.fragil = fragil;
    }

    public Integer getNumeroPaquetes() {
        return numeroPaquetes;
    }

    public void setNumeroPaquetes(Integer numeroPaquetes) {
        this.numeroPaquetes = numeroPaquetes;
    }

    public BigDecimal getCosteTotal() {
        return costeTotal;
    }

    public void setCosteTotal(BigDecimal costeTotal) {
        this.costeTotal = costeTotal;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}
