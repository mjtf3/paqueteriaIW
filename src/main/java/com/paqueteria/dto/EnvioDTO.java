package com.paqueteria.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.paqueteria.model.DistanciaEnum;
import com.paqueteria.model.Envio;
import com.paqueteria.model.EstadoEnum;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;

public class EnvioDTO {

    private Integer id;
    private String localizador;
    private String direccionOrigen;
    private String direccionDestino;
    private EstadoEnum estado;
    private String estadoString;
    private String nombreComprador;
    private String nota;
    private BigDecimal peso;
    private DistanciaEnum distancia;
    private Boolean fragil;
    private Integer numeroPaquetes;
    private BigDecimal costeTotal;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
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

    public EnvioDTO(String localizador, String estadoString, String direccionOrigen,
            String direccionDestino, String fecha) {
        this.localizador = localizador;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.fecha = LocalDate.parse(fecha, formatter);
        this.estadoString = estadoString;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
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

    public String getEstadoString() {
        if (this.estadoString != null) {
            return this.estadoString;
        }
        if (this.estado != null) {
            switch (this.estado) {
                case PENDIENTE:
                    return "EN ALMACEN";
                case RUTA:
                    return "EN REPARTO";
                case ENTREGADO:
                    return "ENTREGADO";
                case AUSENTE:
                    return "AUSENTE";
                case RECHAZADO:
                    return "RECHAZADO";
                default:
                    return "EN ALMACEN";
            }
        }
        return "";
    }

    // Helpers para la vista (Thymeleaf Native Image friendly)
    public boolean isAlmacenOPosterior() {
        String s = getEstadoString();
        return "EN ALMACEN".equals(s) || "EN REPARTO".equals(s) || "ENTREGADO".equals(s) || "AUSENTE".equals(s) || "RECHAZADO".equals(s);
    }

    public boolean isRepartoOPosterior() {
        String s = getEstadoString();
        return "EN REPARTO".equals(s) || "ENTREGADO".equals(s) || "AUSENTE".equals(s) || "RECHAZADO".equals(s);
    }

    public boolean isFinalizado() {
        String s = getEstadoString();
        return "ENTREGADO".equals(s) || "AUSENTE".equals(s) || "RECHAZADO".equals(s);
    }
    public void setEstadoString(String estadoString) {
        this.estadoString = estadoString;
    }
}
