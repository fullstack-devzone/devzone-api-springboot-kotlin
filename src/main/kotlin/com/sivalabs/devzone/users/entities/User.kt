package com.sivalabs.devzone.users.entities

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
@Table(name = "users")
class User : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq", allocationSize = 100)
    var id: Long? = null

    @Column(nullable = false)
    @NotEmpty
    var name: String? = null

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email(message = "Invalid email")
    var email: String? = null

    @Column(nullable = false)
    @NotEmpty
    @Size(min = 4)
    var password: String? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: RoleEnum? = null

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
}
