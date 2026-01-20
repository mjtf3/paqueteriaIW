package com.paqueteria.dto;

import com.paqueteria.model.TipoEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class UsuarioData {

	private Integer id;

	@NotBlank(message = "El nombre de usuario es obligatorio")
	@Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
	private String apodo;

	@NotBlank(message = "El nombre es obligatorio")
	@Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
	private String nombre;

	@NotBlank(message = "El nombre de tienda es obligatorio")
	@Size(min = 2, max = 100, message = "El nombre de tienda debe tener entre 2 y 100 caracteres")
	private String nombreTienda;

	@NotBlank(message = "Los apellidos son obligatorios")
	@Size(min = 2, max = 150, message = "Los apellidos deben tener entre 2 y 150 caracteres")
	private String apellidos;

	@Enumerated(EnumType.STRING)
	private TipoEnum tipo;

	@NotBlank(message = "El correo electrónico es obligatorio")
	@Email(message = "El formato del correo electrónico no es válido")
	private String correo;

	@NotBlank(message = "El teléfono es obligatorio")
	@Pattern(regexp = "^[0-9]{9,15}$", message = "El teléfono debe contener entre 9 y 15 dígitos")
	private String telefono;

	@NotBlank(message = "La contraseña es obligatoria")
	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
		message = "La contraseña debe contener al menos una mayúscula, una minúscula y un número"
	)
	private String contrasena;

	private LocalDate fechaCreacion;
	private BigDecimal pesoMaximo;
	private Boolean activa = true;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public BigDecimal getPesoMaximo() {
		return pesoMaximo;
	}

	public void setPesoMaximo(BigDecimal pesoMaximo) {
		this.pesoMaximo = pesoMaximo;
	}

	public Boolean getActiva() {
		return activa;
	}

	public void setActiva(Boolean activa) {
		this.activa = activa;
	}

	public Boolean isAdmin() {
		return this.tipo == TipoEnum.WEBMASTER;
	}

	public Boolean isTienda() {
		return this.tipo == TipoEnum.CLIENTE;
	}

	public Boolean isRepartidor() {
		return this.tipo == TipoEnum.REPARTIDOR;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UsuarioData)) return false;
		UsuarioData that = (UsuarioData) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
