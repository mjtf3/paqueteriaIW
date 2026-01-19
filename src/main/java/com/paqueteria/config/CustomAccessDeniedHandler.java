package com.paqueteria.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String referer = request.getHeader("Referer");
        // Redirige al Referer o al login, NUNCA a dashboard
        if (referer != null && !referer.isEmpty() && !referer.contains("/dashboard")) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect("/auth/login");
        }
    }
}