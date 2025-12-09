package com.example.arabul;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException, java.io.IOException {
        try {
            String token = request.getHeader("Authorization");
            if (token != null) {

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(JWTService.getKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                RequestContext.setUserId(claims.get("id", Integer.class));
                RequestContext.setUserEmail(claims.get("email", String.class));
            }
            chain.doFilter(request, response);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } finally {
            RequestContext.clear();
        }
    }
}