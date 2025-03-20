package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.exaptions.JwtAuthenticationException;
import com.example.demo.repo.UserRepo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static io.micrometer.common.util.StringUtils.isEmpty;

@Component
public class JwtService extends OncePerRequestFilter {


    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepo userRepo;
    @Autowired
    private UserDetailsService userDetailsService;


    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return antPathMatcher.match("/login", request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            final String authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token;
            String username = "";

            if (authorizationToken.startsWith("Bearer")) {
                token = authorizationToken.replace("Bearer ", "").trim();
                username = jwtUtils.extractUsername(token);
            }


            Users user = userRepo.findByUsername(username);

            if (user == null) {
                throw new RuntimeException("Cannot find user");
            }

            if (!isEmpty(authorizationToken) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userPrinciple = userDetailsService.loadUserByUsername(username);
                if (jwtUtils.isTokenValid(authorizationToken.replace("Bearer ", "").trim(), userPrinciple)) {
                    System.out.print(userPrinciple.getAuthorities());
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userPrinciple, null, userPrinciple.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }

    }


}
