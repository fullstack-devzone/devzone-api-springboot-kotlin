package com.sivalabs.devzone.config

import com.sivalabs.devzone.auth.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class WebSecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
        http.authorizeHttpRequests { authorize ->
            authorize
                .requestMatchers("/favicon.ico","/actuator/**", "/swagger-ui/**", "v3/api-docs/**", "/error").permitAll()
                .requestMatchers("/api/login", "/api/users/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                .anyRequest().authenticated()
        }
        http.sessionManagement {
                s ->
            s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
