package com.zestark.timewatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the application.
 *
 * <p>Configures authentication, authorization, and security settings.
 * Currently configured for development with disabled CSRF and permissive access.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * Configures the security filter chain.
   *
   * @param httpSecurityConfig the HTTP security configuration
   * @return the configured security filter chain
   * @throws Exception if configuration fails
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurityConfig) throws Exception {
    httpSecurityConfig
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sessionConfig ->
            sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(authConfig -> {
          authConfig.requestMatchers("/actuator/**").permitAll();
          authConfig.requestMatchers("/**").permitAll();
        });

    return httpSecurityConfig.build();
  }
}
