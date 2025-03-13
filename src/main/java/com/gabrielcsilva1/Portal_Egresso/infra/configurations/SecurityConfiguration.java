package com.gabrielcsilva1.Portal_Egresso.infra.configurations;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
  @Autowired
  SecurityFilter securityFilter;

  @Value("${cors.allowed-origins}")
  private String allowedOrigins;
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity.csrf(csrf -> csrf.disable())
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(authorize -> {
        authorize
         .requestMatchers(HttpMethod.POST, "/coordinator/session").permitAll()
         .requestMatchers(HttpMethod.POST, "/coordinator").permitAll()
         .requestMatchers(HttpMethod.POST, "/graduate").permitAll()
         .requestMatchers(HttpMethod.POST, "/graduate/session").permitAll()
         .requestMatchers(HttpMethod.POST, "/logout").permitAll()
         .requestMatchers(HttpMethod.GET, "/job-opportunity/unverified").authenticated()
         .requestMatchers(HttpMethod.GET, "/testimonial/unverified").authenticated()
         .requestMatchers(HttpMethod.GET).permitAll()
         .anyRequest().authenticated();
      })
      .logout(logout -> logout
        .logoutSuccessHandler((request, response, authentication) -> {
                // Retorna um status HTTP 200 (OK) sem redirecionamento
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().flush();
            })
            .invalidateHttpSession(true)
            .deleteCookies("jwtToken")
            )
      .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(Arrays.asList(this.allowedOrigins));
      configuration.setAllowedMethods(Arrays.asList("*"));
      configuration.setAllowedHeaders(Arrays.asList("*"));
      configuration.setAllowCredentials(true);
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
  }
}
