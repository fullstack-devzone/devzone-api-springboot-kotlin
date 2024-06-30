package com.sivalabs.devzone.users.domain

import com.sivalabs.devzone.common.exceptions.BadRequestException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun getUserById(id: Long): UserDTO? {
        return userRepository.findById(id)
    }

    fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    @Transactional
    fun createUser(request: CreateUserRequest): UserDTO {
        if (userRepository.existsByEmail(request.email)) {
            throw BadRequestException("Email " + request.email + " is already in use")
        }
        val user = User()
        user.name = request.name
        user.email = request.email
        user.password = passwordEncoder.encode(request.password)
        user.role = RoleEnum.ROLE_USER
        return userRepository.save(user)
    }
}
