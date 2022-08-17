package com.sivalabs.devzone.config.security

import com.sivalabs.devzone.users.models.UserDTO
import org.springframework.security.core.authority.SimpleGrantedAuthority

class SecurityUser(val user: UserDTO) : org.springframework.security.core.userdetails.User(
    user.email,
    user.password,
    setOf(SimpleGrantedAuthority(user.role?.name))
)
