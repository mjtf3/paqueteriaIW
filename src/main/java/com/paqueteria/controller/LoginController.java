package com.paqueteria.controller;

import com.paqueteria.dto.UsuarioData;
import com.paqueteria.model.TipoEnum;
import com.paqueteria.services.UsuarioService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/auth")
@Controller
@RegisterReflectionForBinding(UsuarioData.class)
public class LoginController {

	@Autowired
	UsuarioService usuarioService;

	@GetMapping("/registro")
	public String registrarForm(Model model) {
		model.addAttribute("usuario", new UsuarioData());
		return "registroForm";
	}

	@PostMapping("/registro")
	public String registrarTienda(
		@Valid @ModelAttribute("usuario") UsuarioData usuarioData,
		BindingResult result,
		Model model
	) {
		// Si hay errores de validación, volver al formulario
		if (result.hasErrors()) {
			model.addAttribute("usuario", usuarioData);
			return "registroForm";
		}

		// Verificar si el correo ya existe
		if (usuarioService.findByCorreo(usuarioData.getCorreo()) != null) {
			model.addAttribute("error", "Ya existe un usuario con ese correo electrónico");
			model.addAttribute("usuario", usuarioData);
			return "registroForm";
		}

		usuarioData.setTipo(TipoEnum.CLIENTE);
		usuarioData.setFechaCreacion(LocalDate.now());

		try {
			usuarioService.registrar(usuarioData);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("usuario", usuarioData);
			return "registroForm";
		}
		return "redirect:/auth/login?registroExitoso=true";
	}

	@GetMapping("/login")
	public String loginForm(
		@RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "registroExitoso", required = false) String registroExitoso,
		Model model
	) {
		if (error != null) {
			model.addAttribute("error", "Correo electrónico o contraseña incorrectos");
		}
		if (registroExitoso != null) {
			model.addAttribute("mensaje", "Registro exitoso. Ya puedes iniciar sesión.");
		}
		return "loginForm";
	}

	@GetMapping("/dashboard")
	public String redirectUser(Authentication authentication, Model model) {
		String correo = authentication.getName();
		UsuarioData usuario = usuarioService.findByCorreo(correo);
		return switch (usuario.getTipo()) {
			case CLIENTE -> "redirect:/tienda/" + usuario.getId() + "/apikey";
			case WEBMASTER -> "redirect:/";
			case REPARTIDOR -> "redirect:/";
			default -> "redirect:/";
		};
	}
}
