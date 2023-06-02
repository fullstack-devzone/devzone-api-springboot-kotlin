package com.sivalabs.devzone.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.sivalabs.devzone.config.security.SecurityConfig
import com.sivalabs.devzone.config.security.SecurityUtils
import com.sivalabs.devzone.config.security.TokenHelper
import com.sivalabs.devzone.config.security.WebSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc

@ActiveProfiles("test")
@Import(WebSecurityConfig::class, SecurityConfig::class)
abstract class AbstractWebMvcTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @MockBean
    protected lateinit var userDetailsService: UserDetailsService

    @MockBean
    protected lateinit var tokenHelper: TokenHelper

    @MockBean
    protected lateinit var securityUtils: SecurityUtils

    @MockBean
    protected lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    protected lateinit var objectMapper: ObjectMapper
}
