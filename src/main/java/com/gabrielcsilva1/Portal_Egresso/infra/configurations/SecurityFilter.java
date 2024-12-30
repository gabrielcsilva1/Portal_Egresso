package com.gabrielcsilva1.Portal_Egresso.infra.configurations;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
  @Autowired
  private TokenService tokenService;

  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @SuppressWarnings("null")
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token = this.recoverToken(request);
    
    if (token != null) {
      try {
        String subject = this.tokenService.validateToken(token);

        UserDetails coordinator = this.coordinatorRepository.findById(UUID.fromString(subject)).get();

        var authentication = new UsernamePasswordAuthenticationToken(coordinator, null, coordinator.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (IllegalArgumentException exception) {}
    }

    filterChain.doFilter(request, response);
  }

  private String recoverToken(HttpServletRequest request) {
    var authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader == null) {
      return null;
    }

    return authorizationHeader.replace("Bearer ", "");
  }
  
}
