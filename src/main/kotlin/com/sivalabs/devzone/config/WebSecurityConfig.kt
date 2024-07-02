package com.sivalabs.devzone.config

import com.sivalabs.devzone.auth.TokenAuthFilter
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
    private val tokenAuthFilter: TokenAuthFilter,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
        http.authorizeHttpRequests { authorize ->
            authorize
                .requestMatchers("/actuator/**", "/error").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/posts").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/posts/*").authenticated()
                .requestMatchers("/api/me").authenticated()
                .anyRequest().permitAll()
        }
        http.sessionManagement {
                s ->
            s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        http.addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
