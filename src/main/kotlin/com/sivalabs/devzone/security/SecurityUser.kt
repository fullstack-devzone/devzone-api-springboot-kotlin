package com.sivalabs.devzone.security

import com.sivalabs.devzone.users.domain.User
import org.springframework.security.core.authority.SimpleGrantedAuthority

class SecurityUser(val user: User) : org.springframework.security.core.userdetails.User(
    user.email,
    user.password,
    setOf(SimpleGrantedAuthority(user.role.name)),
)
