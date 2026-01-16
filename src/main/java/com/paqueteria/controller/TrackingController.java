package com.paqueteria.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paqueteria.model.TrackingStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class TrackingController {

    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    @GetMapping("/tracking")
    public ResponseEntity<Map<String, Object>> getTracking(@RequestParam String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código de seguimiento es obligatorio");
        }

        // Simulación simple: determinamos el estado en función del último carácter
        String trimmed = code.trim();
        char last = trimmed.charAt(trimmed.length() - 1);
        TrackingStatus status;
        if (Character.isDigit(last)) {
            int digit = Character.getNumericValue(last);
            if (digit % 3 == 0) status = TrackingStatus.ENTREGADO;
            else if (digit % 3 == 1) status = TrackingStatus.EN_REPARTO;
            else status = TrackingStatus.EN_ALMACEN;
        } else {
            status = TrackingStatus.EN_REPARTO;
        }

        // Construimos historial con timestamps simulados
        List<Map<String, String>> events = new ArrayList<>();
        long now = Instant.now().getEpochSecond();

        Map<String, String> e1 = new HashMap<>();
        e1.put("status", TrackingStatus.EN_ALMACEN.name());
        e1.put("label", TrackingStatus.EN_ALMACEN.getLabel());
        e1.put("time", TF.format(Instant.ofEpochSecond(now - 3600 * 48))); // hace 48h
        events.add(e1);

        if (status == TrackingStatus.EN_REPARTO || status == TrackingStatus.ENTREGADO) {
            Map<String, String> e2 = new HashMap<>();
            e2.put("status", TrackingStatus.EN_REPARTO.name());
            e2.put("label", TrackingStatus.EN_REPARTO.getLabel());
            e2.put("time", TF.format(Instant.ofEpochSecond(now - 3600 * 5))); // hace 5h
            events.add(e2);
        }

        if (status == TrackingStatus.ENTREGADO) {
            Map<String, String> e3 = new HashMap<>();
            e3.put("status", TrackingStatus.ENTREGADO.name());
            e3.put("label", TrackingStatus.ENTREGADO.getLabel());
            e3.put("time", TF.format(Instant.ofEpochSecond(now - 3600))); // hace 1h
            events.add(e3);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("code", trimmed);
        body.put("status", status.name());
        body.put("label", status.getLabel());
        body.put("events", events);

        return ResponseEntity.ok(body);
    }
}
