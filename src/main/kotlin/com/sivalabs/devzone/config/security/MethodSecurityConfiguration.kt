package com.sivalabs.devzone.config.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
class MethodSecurityConfiguration(private val roleHierarchy: RoleHierarchy) : GlobalMethodSecurityConfiguration() {

    override fun createExpressionHandler(): MethodSecurityExpressionHandler {
        val expressionHandler = super.createExpressionHandler() as DefaultMethodSecurityExpressionHandler
        expressionHandler.setRoleHierarchy(roleHierarchy)
        return expressionHandler
    }
}
