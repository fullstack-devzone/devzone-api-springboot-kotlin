package com.sivalabs.devzone.security

import com.sivalabs.devzone.users.domain.User
import com.sivalabs.devzone.users.domain.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service("userDetailsService")
class SecurityUserDetailsService(private val userService: UserService) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails? {
        return userService
            .getUserByEmail(username)
            .map { user: User -> SecurityUser(user) }
            .orElseThrow {
                UsernameNotFoundException(
                    "No user found with username $username",
                )
            }
    }
}
