package com.sivalabs.devzone.users.services

import com.sivalabs.devzone.common.exceptions.ApplicationException
import com.sivalabs.devzone.users.entities.RoleEnum
import com.sivalabs.devzone.users.models.UserDTO
import com.sivalabs.devzone.users.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional(readOnly = true)
    fun getUserById(id: Long): Optional<UserDTO> {
        return userRepository.findById(id).map { u -> UserDTO.fromEntity(u) }
    }

    @Transactional(readOnly = true)
    fun getUserByEmail(email: String): Optional<UserDTO> {
        return userRepository.findByEmail(email).map { u -> UserDTO.fromEntity(u) }
    }

    fun createUser(user: UserDTO): UserDTO {
        if (userRepository.existsByEmail(user.email!!)) {
            throw ApplicationException("Email " + user.email + " is already in use")
        }
        user.password = passwordEncoder.encode(user.password)
        val userEntity = user.toEntity()
        userEntity.role = RoleEnum.ROLE_USER
        return UserDTO.fromEntity(userRepository.save(userEntity))
    }
}
