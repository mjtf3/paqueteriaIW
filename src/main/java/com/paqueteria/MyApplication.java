package com.paqueteria;

import java.util.Map;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MyApplication {

	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}

	@RequestMapping("/api/tarifas/general")
    public Map<String, Object> about() {
        // Distancia data
        List<Map<String, Object>> distancia = List.of(
            Map.of("tipo", "Ciudad", "precio", 50.0f),
            Map.of("tipo", "Provincial", "precio", 100.0f),
            Map.of("tipo", "Nacional", "precio", 200.0f),
            Map.of("tipo", "Internacional", "precio", 500.0f)
        );

        // RangoPeso data
        List<Map<String, Object>> rangoPeso = List.of(
            Map.of("min", 0,  "max", 10,  "precio", 10.0f, "descripcion", "< 10kg"),
            Map.of("min", 10, "max", 20,  "precio", 20.0f, "descripcion", "10kg - 20kg"),
            Map.of("min", 20, "max", 40,  "precio", 35.0f, "descripcion", "20kg - 40kg"),
            Map.of("min", 40, "max", Integer.MAX_VALUE, "precio", 50.0f, "descripcion", "> 40kg")
        );

        return Map.of(
            "distancia", distancia,
            "rangoPeso", rangoPeso
        );
    }


	public static void main(String[] args) {
		SpringApplication.run(MyApplication.class, args);
	}

}
