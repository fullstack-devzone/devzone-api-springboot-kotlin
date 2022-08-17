package com.sivalabs.devzone.config

import com.sivalabs.devzone.config.argresolvers.CurrentUserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(private val currentUserArgumentResolver: CurrentUserArgumentResolver) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver?>) {
        resolvers.add(currentUserArgumentResolver)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowedOriginPatterns("*")
            .allowCredentials(true)
    }
}
