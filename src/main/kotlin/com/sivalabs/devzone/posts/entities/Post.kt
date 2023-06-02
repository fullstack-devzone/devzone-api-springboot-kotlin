package com.sivalabs.devzone.posts.entities

import com.sivalabs.devzone.users.entities.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
class Post : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_id_generator")
    @SequenceGenerator(name = "post_id_generator", sequenceName = "post_id_seq", allocationSize = 100)
    var id: Long? = null

    @Column(nullable = false)
    var url: String? = null

    @Column(nullable = false)
    var title: String? = null

    var content: String? = null

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: User? = null

    @Column(updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(insertable = false)
    var updatedAt: LocalDateTime? = null

    @PrePersist
    fun onCreate() {
        createdAt = LocalDateTime.now()
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
