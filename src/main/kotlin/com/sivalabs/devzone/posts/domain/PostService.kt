package com.sivalabs.devzone.posts.domain

import com.sivalabs.devzone.common.models.PagedResult
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
) {
    private val log = KotlinLogging.logger {}

    companion object {
        const val PAGE_SIZE = 10
    }

    fun getAllPosts(page: Int): PagedResult<PostDTO> {
        return postRepository.findBy(page, PAGE_SIZE)
    }

    fun searchPosts(
        query: String,
        page: Int,
    ): PagedResult<PostDTO> {
        return postRepository.search(query, page, PAGE_SIZE)
    }

    fun getPostById(id: Long): PostDTO? {
        log.debug { "get post by id=$id" }
        return postRepository.findById(id)
    }

    @Transactional
    fun createPost(request: CreatePostCmd): Long {
        log.debug { "create post with url=${request.url}" }
        val post =
            Post(
                null,
                request.url,
                request.title,
                request.content,
                request.userId,
                LocalDateTime.now(),
                null,
            )
        return postRepository.save(post)
    }

    @Transactional
    fun deletePost(id: Long) {
        log.debug { "delete post by id=$id" }
        postRepository.deleteById(id)
    }

    @Transactional
    fun deleteAllPosts() {
        log.debug { "delete all posts" }
        postRepository.deleteAll()
    }
}
