package com.paqueteria.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "envio", indexes = {
    // el indice se crea automaticamente al poner unique=true en la columna pero asi queda mas claro
    @Index(name = "idx_envio_localizador", columnList = "localizador")
})
public class Envio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "El localizador no puede ser nulo")
    @Column(nullable = false, unique = true)
    private String localizador;
    
    @NotNull(message = "La dirección de origen no puede ser nula")
    @Column(name = "direccion_origen", nullable = false)
    private String direccionOrigen;
    
    @NotNull(message = "La dirección de destino no puede ser nula")
    @Column(name = "direccion_destino", nullable = false)
    private String direccionDestino;
    
    @NotNull(message = "El estado no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnum estado = EstadoEnum.PENDIENTE;
    
    @NotNull(message = "El nombre del comprador no puede ser nulo")
    @Column(name = "nombre_comprador", nullable = false)
    private String nombreComprador;
    
    @Column(length = 500)
    private String nota;
    
    @NotNull(message = "El peso no puede ser nulo")
    @Column(nullable = false)
    private BigDecimal peso;
    
    @NotNull(message = "La distancia no puede ser nula")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DistanciaEnum distancia;
    
    @Column()
    private Boolean fragil = false;
    
    @NotNull(message = "El número de paquetes no puede ser nulo")
    @Column(name = "numero_paquetes", nullable = false)
    private Integer numeroPaquetes;
    
    @NotNull(message = "El coste total no puede ser nulo")
    @Column(name = "coste_total", nullable = false)
    private BigDecimal costeTotal;
    
    //la fecha no es modificable
    @NotNull(message = "La fecha no puede ser nula")
    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fecha;
    
    @NotNull(message = "El usuario no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "ruta_id")
    private Ruta ruta;
    
    @NotNull(message = "La tarifa de distancia no puede ser nula")
    @ManyToOne
    @JoinColumn(name = "tarifa_distancia_id", nullable = false)
    private TarifaDistancia tarifaDistancia;
    
    @NotNull(message = "La tarifa de rango de peso no puede ser nula")
    @ManyToOne
    @JoinColumn(name = "tarifa_rango_peso_id", nullable = false)
    private TarifaRangoPeso tarifaRangoPeso;
    
    // Constructors
    public Envio() {}
    
    public Envio(String localizador, String direccionOrigen, String direccionDestino,
                 String nombreComprador, BigDecimal peso, DistanciaEnum distancia,
                 Integer numeroPaquetes, BigDecimal costeTotal, Usuario usuario,
                 TarifaDistancia tarifaDistancia, TarifaRangoPeso tarifaRangoPeso) {
        this.localizador = localizador;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.nombreComprador = nombreComprador;
        this.peso = peso;
        this.distancia = distancia;
        this.numeroPaquetes = numeroPaquetes;
        this.costeTotal = costeTotal;
        this.usuario = usuario;
        this.tarifaDistancia = tarifaDistancia;
        this.tarifaRangoPeso = tarifaRangoPeso;
        this.estado = EstadoEnum.PENDIENTE;
        this.fragil = false;
        this.fecha = LocalDate.now();
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
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
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Ruta getRuta() {
        return ruta;
    }
    
    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }
    
    public TarifaDistancia getTarifaDistancia() {
        return tarifaDistancia;
    }
    
    public void setTarifaDistancia(TarifaDistancia tarifaDistancia) {
        this.tarifaDistancia = tarifaDistancia;
    }
    
    public TarifaRangoPeso getTarifaRangoPeso() {
        return tarifaRangoPeso;
    }
    
    public void setTarifaRangoPeso(TarifaRangoPeso tarifaRangoPeso) {
        this.tarifaRangoPeso = tarifaRangoPeso;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Envio envio = (Envio) o;
        if (id != null && envio.id != null)
            return id.equals(envio.id);
        return localizador.equals(envio.localizador);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(localizador);
    }
}
