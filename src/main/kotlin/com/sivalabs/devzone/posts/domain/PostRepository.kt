package com.sivalabs.devzone.posts.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findByTitleContainingIgnoreCase(
        @Param("query") query: String,
        pageable: Pageable,
    ): Page<Post>
}
