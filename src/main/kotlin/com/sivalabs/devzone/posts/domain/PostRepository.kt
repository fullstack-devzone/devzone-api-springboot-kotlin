package com.sivalabs.devzone.posts.domain

import com.sivalabs.devzone.common.models.PagedResult
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull
import kotlin.math.ceil

@Repository
class PostRepository(private val jdbcClient: JdbcClient) {
    fun findBy(
        page: Int,
        pageSize: Int,
    ): PagedResult<PostDTO> {
        val totalElements = jdbcClient.sql("select count(*) from posts").query(Long::class.java).single()
        val totalPages = ceil((totalElements.toDouble() / pageSize.toDouble())).toInt()
        val sql = """
            select p.id, p.url, p.title, p.content, p.created_at, p.updated_at,
                    u.id as createdUserId, u.name as createdUserName
            from posts p left join users u on u.id = p.created_by
            order by p.created_at desc
            limit $pageSize offset ${(page - 1) * pageSize}
            """
        val content = jdbcClient.sql(sql).query(PostRowMapper).list()
        return PagedResult(
            content,
            totalElements,
            page,
            totalPages,
        )
    }

    fun search(
        query: String,
        page: Int,
        pageSize: Int,
    ): PagedResult<PostDTO> {
        val totalElements = jdbcClient.sql("select count(*) from posts").query(Long::class.java).single()
        val totalPages = ceil((totalElements.toDouble() / pageSize.toDouble())).toInt()
        val sql = """
            select p.id, p.url, p.title, p.content, p.created_at, p.updated_at,
                    u.id as createdUserId, u.name as createdUserName
            from posts p left join users u on u.id = p.created_by
            where p.title like :query
            order by p.created_at desc
            limit :pageSize offset :offset
            """
        val content =
            jdbcClient.sql(sql)
                .param("query", "%$query%")
                .param("pageSize", pageSize)
                .param("offset", (page - 1) * pageSize)
                .query(PostRowMapper).list()
        return PagedResult(
            content,
            totalElements,
            page,
            totalPages,
        )
    }

    fun findById(id: Long): PostDTO? {
        val sql = """
            select p.id, p.url, p.title, p.content, p.created_at, p.updated_at,
                    u.id as createdUserId, u.name as createdUserName
            from posts p left join users u on u.id = p.created_by
            where p.id = :id
            """
        return jdbcClient.sql(sql)
            .param("id", id)
            .query(PostRowMapper).optional().getOrNull()
    }

    fun save(post: Post): Long {
        val sql = """insert into posts(url, title, content, created_by, created_at) 
            values(:url,:title, :content, :created_by, :created_at) returning id
            """
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcClient.sql(sql)
            .param("url", post.url)
            .param("title", post.title)
            .param("content", post.content)
            .param("created_by", post.createdBy)
            .param("created_at", LocalDateTime.now())
            .update(keyHolder)
        return keyHolder.key?.toLong()!!
    }

    fun deleteById(id: Long) {
        jdbcClient.sql("delete from posts where id = :id").param("id", id)
    }

    fun deleteAllInBatch() {
        jdbcClient.sql("delete from posts").update()
    }
}

object PostRowMapper : RowMapper<PostDTO> {
    override fun mapRow(
        rs: ResultSet,
        rowNum: Int,
    ): PostDTO {
        val postDTO = PostDTO()
        postDTO.id = rs.getLong("id")
        postDTO.url = rs.getString("url")
        postDTO.title = rs.getString("title")
        postDTO.content = rs.getString("content")
        postDTO.createdBy =
            PostCreator(
                rs.getLong("createdUserId"),
                rs.getString("createdUserName"),
            )
        postDTO.createdAt = rs.getTimestamp("created_at").toLocalDateTime()
        postDTO.updatedAt = rs.getTimestamp("updated_at")?.toLocalDateTime()

        return postDTO
    }
}
