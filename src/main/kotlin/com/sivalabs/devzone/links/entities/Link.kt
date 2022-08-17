package com.sivalabs.devzone.links.entities

import com.sivalabs.devzone.users.entities.User
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "links")
class Link : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "link_id_generator")
    @SequenceGenerator(name = "link_id_generator", sequenceName = "link_id_seq", allocationSize = 100)
    var id: Long? = null

    @Column(nullable = false)
    var url: String? = null

    @Column(nullable = false)
    var title: String? = null

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "link_tag",
        joinColumns = [JoinColumn(name = "link_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id", referencedColumnName = "id")]
    )
    var tags: MutableSet<Tag> = mutableSetOf()

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
