package com.paqueteria.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.DriverManager;
import java.sql.Connection;

@RestController
@RequestMapping("/test")
public class example {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @GetMapping("/conexion")
    public ResponseEntity<String> testConexion() {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
            return ResponseEntity.ok("✅ Conexión exitosa");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error: " + e.getMessage());
        }
    }
}