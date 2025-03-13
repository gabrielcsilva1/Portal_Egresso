package com.gabrielcsilva1.Portal_Egresso.infra.configurations;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.IGenericUser;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TokenService;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.RoleEnum;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
  @Autowired
  private TokenService tokenService;

  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @Autowired
  private GraduateRepository graduateRepository;

  @SuppressWarnings("null")
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token = this.recoverToken(request);
    
    if (token != null) {
      try {
        DecodedJWT decodedJWT = this.tokenService.validateToken(token);
        
        if (decodedJWT == null) {
          throw new IllegalArgumentException("Invalid token");
        }

        String userId = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

        List<SimpleGrantedAuthority> grantedAuthorities = roles.stream()
          .map(role -> new SimpleGrantedAuthority("ROLE_"+role))
          .toList();

        IGenericUser authenticatedUser = this.findAuthenticateUser(userId, roles);

        var authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, grantedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (IllegalArgumentException exception) {}
    }

    filterChain.doFilter(request, response);
  }

  private String recoverToken(HttpServletRequest request) {
    String token = null;

    if (request.getCookies() == null) {
      return null;
    }
    
    for (Cookie cookie : request.getCookies()) {
      if (cookie.getName().equals("jwtToken")) {
        token = cookie.getValue();
      }
    }

    return token;
  }

  private IGenericUser findAuthenticateUser(String id, List<String> roles) {
    if (roles.contains(RoleEnum.COORDINATOR.name())) {
      var coordinator = this.coordinatorRepository.findById(UUID.fromString(id));
      if (coordinator.isPresent()) {
        return coordinator.get();
      }
    }
    else if (roles.contains(RoleEnum.GRADUATE.name())) {
      var graduate = this.graduateRepository.findById(UUID.fromString(id));
      if (graduate.isPresent()) {
        return graduate.get();
      }
    }

    throw new IllegalArgumentException("Bad Request");
  }
  
}
