package com.paqueteria.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/seguimiento")
    public String seguimiento(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "error", required = false) String error,
            Model model) {

        model.addAttribute("code", code);
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        return "seguimiento";
    }
}

