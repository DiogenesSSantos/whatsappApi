package com.github.dio.messageira.infraestruct.sessionsingress;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class SessaoFiltro implements Filter {
    private static final String COOKIE_NAME = "device-session";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // Verifica se o cookie já existe
        String sessionCookie = getCookieValue(httpRequest, COOKIE_NAME);
        if (sessionCookie == null) {
            // Gera um ID único para o dispositivo/navegador
            String uniqueSessionId = UUID.randomUUID().toString();

            // Define o cookie com o ID único
            httpResponse.addCookie(createSessionCookie(uniqueSessionId));
        }

        // Prossegue com a requisição
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private javax.servlet.http.Cookie createSessionCookie(String sessionId) {
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(COOKIE_NAME, sessionId);
        cookie.setPath("/");
        cookie.setMaxAge(86400); // Expira em 1 dia
        return cookie;
    }




    @Override
    public void destroy() {

    }
}
