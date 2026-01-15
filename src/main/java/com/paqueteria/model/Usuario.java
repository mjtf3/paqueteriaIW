package com.paqueteria.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "usuario")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column()
    private String apodo;
    
    @NotNull(message = "El nombre no puede ser nulo")
    @Column(nullable = false)
    private String nombre;
    
    @Column(name = "nombre_tienda")
    private String nombreTienda;
    
    @NotNull(message = "Los apellidos no pueden ser nulos")
    @Column(nullable = false)
    private String apellidos;
    
    @NotNull(message = "El tipo no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEnum tipo;
    
    @NotNull(message = "El correo no puede ser nulo")
    @Column(nullable = false, unique = true)
    private String correo;
    
    @Column()
    private String telefono;
    
    @NotNull(message = "La contraseña no puede ser nula")
    @Column(nullable = false)
    private String contrasena;
    
    @NotNull(message = "La fecha de creación no puede ser nula")
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;
    
    @NotNull(message = "El peso máximo no puede ser nulo")
    @Column(name = "peso_maximo", nullable = false)
    private Float pesoMaximo;
    
    @NotNull(message = "El estado activa no puede ser nulo")
    @Column(nullable = false)
    private Boolean activa = true;
    
    @OneToMany(mappedBy = "usuario")
    private final List<API> apis = new ArrayList<>();
    
    @OneToMany(mappedBy = "usuario")
    private final List<Ruta> rutas = new ArrayList<>();
    
    @OneToMany(mappedBy = "usuario")
    private final List<Envio> enviosRealizados = new ArrayList<>();
    
    // Constructors
    public Usuario() {}
    
    public Usuario(String apodo, String nombre, String apellidos, TipoEnum tipo, String correo, String telefono, String contrasena, Float pesoMaximo) {
        this.apodo = apodo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.tipo = tipo;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.pesoMaximo = pesoMaximo;
        this.fechaCreacion = LocalDate.now();
        this.activa = true;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public String getApodo() {
        return apodo;
    }
    
    public void setApodo(String apodo) {
        this.apodo = apodo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombreTienda() {
        return nombreTienda;
    }
    
    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public TipoEnum getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoEnum tipo) {
        this.tipo = tipo;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }
    
    // Método calculado - no hay campo en BD
    public Integer getEnvios() {
        return enviosRealizados.size();
    }
    
    public Float getPesoMaximo() {
        return pesoMaximo;
    }
    
    public void setPesoMaximo(Float pesoMaximo) {
        this.pesoMaximo = pesoMaximo;
    }
    
    public Boolean getActiva() {
        return activa;
    }
    
    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
    
    public List<API> getApis() {
        return apis;
    }
    
    // Helper methods para manejar relación bidireccional
    public void addApi(API api) {
        if (this.apis.contains(api)) return;
        this.apis.add(api);
        api.setUsuario(this);
    }
    
    public void removeApi(API api) {
        this.apis.remove(api);
        api.setUsuario(null);
    }
    
    public List<Ruta> getRutas() {
        return rutas;
    }
    
    // Helper methods para manejar relación bidireccional
    public void addRuta(Ruta ruta) {
        if (this.rutas.contains(ruta)) return;
        this.rutas.add(ruta);
        ruta.setUsuario(this);
    }
    
    public void removeRuta(Ruta ruta) {
        this.rutas.remove(ruta);
        ruta.setUsuario(null);
    }
    
    public List<Envio> getEnviosRealizados() {
        return enviosRealizados;
    }
    
    // Helper methods para manejar relación bidireccional
    public void addEnvio(Envio envio) {
        if (this.enviosRealizados.contains(envio)) return;
        this.enviosRealizados.add(envio);
        envio.setUsuario(this);
    }
    
    public void removeEnvio(Envio envio) {
        this.enviosRealizados.remove(envio);
        envio.setUsuario(null);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        if (id != null && usuario.id != null)
            return id.equals(usuario.id);
        return correo.equals(usuario.correo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(correo);
    }
}
