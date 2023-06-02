package com.sivalabs.devzone.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class WebSecurityConfig(
    private val tokenAuthenticationFilter: TokenAuthenticationFilter,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
        http.cors { cors -> cors.disable() }
        http.authorizeHttpRequests { authorize ->
            authorize
                .requestMatchers("/actuator/**", "/error")
                .permitAll()
                .anyRequest()
                .permitAll()
        }
        http.sessionManagement {
                s ->
            s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
