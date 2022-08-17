package com.sivalabs.devzone.links.repositories

import com.sivalabs.devzone.links.entities.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TagRepository : JpaRepository<Tag, Long> {
    fun findByName(tag: String): Optional<Tag>
}
