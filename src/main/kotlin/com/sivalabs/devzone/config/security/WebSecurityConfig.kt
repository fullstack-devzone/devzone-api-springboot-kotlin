package com.sivalabs.devzone.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.access.expression.SecurityExpressionHandler
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.FilterInvocation
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport

@Configuration
@Import(SecurityProblemSupport::class)
class WebSecurityConfig(
    private val authenticationManager: AuthenticationManager,
    private val tokenAuthenticationFilter: TokenAuthenticationFilter,
    private val roleHierarchy: RoleHierarchy,
    private val problemSupport: SecurityProblemSupport
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
        http.cors().disable()
        http.authorizeRequests()
            .expressionHandler(webExpressionHandler(roleHierarchy))
            .antMatchers("/actuator/**", "/api/auth/**")
            .permitAll()
        http.authenticationManager(authenticationManager)
        http.exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter::class.java)
        return http.build()
    }

    private fun webExpressionHandler(roleHierarchy: RoleHierarchy): SecurityExpressionHandler<FilterInvocation> {
        val defaultWebSecurityExpressionHandler = DefaultWebSecurityExpressionHandler()
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy)
        return defaultWebSecurityExpressionHandler
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
                web: WebSecurity ->
            web.ignoring()
                .antMatchers("/webjars/**", "/static/**")
        }
    }
}
