package com.sivalabs.devzone.users.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq", allocationSize = 100)
    var id: Long? = null

    @Column(nullable = false)
    @NotEmpty
    var name: String = ""

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email(message = "Invalid email")
    var email: String = ""

    @Column(nullable = false)
    @NotEmpty
    @Size(min = 4)
    var password: String = ""

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: RoleEnum = RoleEnum.ROLE_USER

    @Column(updatable = false)
    private var createdAt = LocalDateTime.now()

    @Column(insertable = false)
    private var updatedAt = LocalDateTime.now()

    @PrePersist
    fun onCreate() {
        createdAt = LocalDateTime.now()
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }

    fun isCurrentUserAdmin(): Boolean {
        return isUserHasAnyRole(RoleEnum.ROLE_ADMIN)
    }

    private fun isUserHasAnyRole(vararg roles: RoleEnum): Boolean {
        return listOf(*roles).contains(this.role)
    }
}
