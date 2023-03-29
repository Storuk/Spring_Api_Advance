package com.epam.esm.config;

import com.epam.esm.enums.Role;
import com.epam.esm.exceptions.exceptionhandler.DelegatedAuthenticationEntryPoint;
import com.epam.esm.exceptions.exceptionhandler.RestAccessDeniedHandler;
import com.epam.esm.jwt.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/authentication/**")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/gift-certificates", "/gift-certificates/**", "/tags", "/tags/**","/actuator/health")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/orders", "/users").hasAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/users/by-user-id", "/orders/by-user-id").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/users/by-user-id-for-admin/**", "/orders/by-user-id-for-admin/**").hasAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.POST, "/gift-certificates", "/tags").hasAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.POST, "/orders/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .requestMatchers(HttpMethod.PATCH, "/gift-certificates/**").hasAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/gift-certificates/**", "/tags/**").hasAuthority(Role.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(delegatedAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler);
        return httpSecurity.build();
    }
}