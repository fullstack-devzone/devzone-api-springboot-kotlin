package com.sivalabs.devzone.config.security

import com.sivalabs.devzone.users.models.UserDTO
import com.sivalabs.devzone.users.services.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service("userDetailsService")
class SecurityUserDetailsService(private val userService: UserService) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails? {
        return userService
            .getUserByEmail(username)
            .map { user: UserDTO -> SecurityUser(user) }
            .orElseThrow {
                UsernameNotFoundException(
                    "No user found with username $username"
                )
            }
    }
}
