package com.sivalabs.devzone.auth

import com.sivalabs.devzone.users.domain.UserDetailsDTO
import com.sivalabs.devzone.users.domain.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service("userDetailsService")
class SecurityUserDetailsService(private val userService: UserService) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails? {
        return userService.getUserByEmail(username)
            ?.let { user: UserDetailsDTO ->
                SecurityUser(
                    user.id,
                    user.name,
                    user.email,
                    user.password,
                    user.role,
                )
            }
            ?: throw UsernameNotFoundException("No user found with username $username")
    }
}
