package com.sivalabs.devzone.config.security

import com.sivalabs.devzone.common.logging.logger
import com.sivalabs.devzone.users.entities.RoleEnum
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl

@Configuration
class RoleHierarchyConfig {
    companion object {
        private val log = logger()
    }

    @Bean
    fun roleHierarchy(): RoleHierarchy {
        val roleHierarchy = RoleHierarchyImpl()
        roleHierarchy.setHierarchy(RoleEnum.getRoleHierarchy())
        log.debug("RoleHierarchy: {}", RoleEnum.getRoleHierarchy())
        return roleHierarchy
    }
}
