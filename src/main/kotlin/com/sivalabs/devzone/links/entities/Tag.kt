package com.sivalabs.devzone.links.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "tags")
class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_generator")
    @SequenceGenerator(name = "tag_id_generator", sequenceName = "tag_id_seq", allocationSize = 100)
    var id: Long? = null

    @Column(nullable = false, unique = true)
    var name: String = ""

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    var links: Set<Link> = mutableSetOf()

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
