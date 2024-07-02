package com.sivalabs.devzone.users.domain

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@Repository
class UserRepository(private val jdbcClient: JdbcClient) {
    fun findById(userId: Long): UserDTO? {
        return jdbcClient.sql("select id, name, email, role from users where id = :id")
            .param("id", userId)
            .query(UserDTO::class.java).optional().getOrNull()
    }

    fun findByEmail(email: String): UserDetailsDTO? {
        return jdbcClient.sql("select id, name, email, password, role from users where email = :email")
            .param("email", email)
            .query(UserDetailsDTO::class.java).optional().getOrNull()
    }

    fun existsByEmail(email: String): Boolean {
        return jdbcClient.sql("select case when count(id) > 0 then 1 else 0 end from users where email = :email")
            .param("email", email)
            .query(Boolean::class.java).single()
    }

    fun save(user: User): UserDTO {
        val sql = """insert into users(name, email, password, role, created_at) 
            values(:name,:email, :password, :role, :createdAt) returning id
            """
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcClient.sql(sql)
            .param("name", user.name)
            .param("email", user.email)
            .param("password", user.password)
            .param("role", user.role.name)
            .param("createdAt", LocalDateTime.now())
            .update(keyHolder)
        val id = keyHolder.key?.toLong()
        return UserDTO(id!!, user.name, user.email, user.role)
    }
}
