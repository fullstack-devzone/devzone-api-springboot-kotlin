package com.sivalabs.devzone.users.services

import com.sivalabs.devzone.common.exceptions.BadRequestException
import com.sivalabs.devzone.users.entities.RoleEnum
import com.sivalabs.devzone.users.entities.User
import com.sivalabs.devzone.users.models.CreateUserRequest
import com.sivalabs.devzone.users.models.UserDTO
import com.sivalabs.devzone.users.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional(readOnly = true)
    fun getUserById(id: Long): Optional<UserDTO?> {
        return userRepository.findById(id).map(UserDTO::fromEntity)
    }

    @Transactional(readOnly = true)
    fun getUserByEmail(email: String): Optional<User> {
        return userRepository.findByEmail(email)
    }

    fun createUser(createUserRequest: CreateUserRequest): UserDTO {
        if (userRepository.existsByEmail(createUserRequest.email!!)) {
            throw BadRequestException("Email " + createUserRequest.email + " is already in use")
        }
        val user = User()
        user.name = createUserRequest.name
        user.email = createUserRequest.email
        user.password = passwordEncoder.encode(createUserRequest.password)
        user.role = RoleEnum.ROLE_USER
        return UserDTO.fromEntity(userRepository.save(user))
    }
}
