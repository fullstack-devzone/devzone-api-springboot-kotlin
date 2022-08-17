package com.sivalabs.devzone.links.repositories

import com.sivalabs.devzone.links.entities.Link
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface LinkRepository : JpaRepository<Link, Long> {

    @Query("select l.id from Link l")
    fun fetchLinkIds(pageable: Pageable): Page<Long>

    @Query("select l.id from Link l where lower(l.title) like lower(concat('%', :query,'%'))")
    fun fetchLinkIdsByTitleContainingIgnoreCase(
        @Param("query") query: String,
        pageable: Pageable
    ): Page<Long>

    @Query("select l.id from Link l LEFT JOIN l.tags t where t.name=?1")
    fun fetchLinkIdsByTag(tagName: String, pageable: Pageable): Page<Long>

    @Query(
        "select DISTINCT l from Link l LEFT JOIN FETCH l.tags join fetch l.createdBy where l.id" +
            " in ?1"
    )
    fun findLinksWithTags(linkIds: List<Long>, sort: Sort): List<Link>
}
