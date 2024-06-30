package com.sivalabs.devzone.posts.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = [ "createdBy" ])
    fun findAllBy(pageable: Pageable): Page<Post>

    @EntityGraph(attributePaths = [ "createdBy" ])
    fun findByTitleContainingIgnoreCase(
        @Param("query") query: String,
        pageable: Pageable,
    ): Page<Post>
}
