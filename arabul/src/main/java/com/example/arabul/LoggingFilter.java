package com.example.arabul;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Date;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException, java.io.IOException {

        String method = request.getMethod();
        String path = request.getRequestURI();
        Date date = new Date();

        try {
            System.out.println("STARTED..");
            System.out.println("Method: " + method);
            System.out.println("Path: " + path);
            System.out.println("Date: " + date);

            chain.doFilter(request, response);
        } finally {
            System.out.println("Method: " + method);
            System.out.println("Path: " + path);
            System.out.println("Date: " + date);
            System.out.println("ENDED");
        }
    }
}
