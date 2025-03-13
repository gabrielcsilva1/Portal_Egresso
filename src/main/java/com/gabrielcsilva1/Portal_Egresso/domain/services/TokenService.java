package com.gabrielcsilva1.Portal_Egresso.domain.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.RoleEnum;

@Service
public class TokenService {
  @Value("${api.security.token.secret}")
  private String secret;

  public String generateToken(String subject, List<RoleEnum> roles) {
    List<String> roleNames = roles.stream()
            .map((role) -> role.name()) // Pega apenas o nome da role
            .toList();
    try {
      Algorithm algorithm = Algorithm.HMAC256(this.secret);
      String token = JWT.create()
        .withSubject(subject)
        .withClaim("roles", roleNames)
        .withExpiresAt(this.generateExpirationDate())
        .sign(algorithm);

      return token;
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Error with generating token: " + exception);
    }
  }

  public DecodedJWT validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(this.secret);
      return JWT.require(algorithm)
        .build()
        .verify(token);
    } catch (JWTVerificationException exception) {
      return null;
    }
  }

  private Instant generateExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }
}
