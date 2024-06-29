package com.sivalabs.devzone.posts.domain

import com.sivalabs.devzone.common.models.PagedResult
import com.sivalabs.devzone.users.domain.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
) {
    private val log = KotlinLogging.logger {}

    companion object {
        const val PAGE_SIZE = 10
    }

    @Transactional(readOnly = true)
    fun getAllPosts(page: Int): PagedResult<PostDTO> {
        val pageable: Pageable =
            PageRequest.of(
                if (page < 1) 0 else page - 1,
                PAGE_SIZE,
                Sort.Direction.DESC,
                "createdAt",
            )
        val pageOfPosts = postRepository.findAll(pageable).map(PostDTO::from)
        return PagedResult(
            pageOfPosts.content,
            pageOfPosts.totalElements,
            pageOfPosts.number + 1,
            pageOfPosts.totalPages,
        )
    }

    @Transactional(readOnly = true)
    fun searchPosts(
        query: String,
        page: Int,
    ): PagedResult<PostDTO> {
        val pageable: Pageable =
            PageRequest.of(
                if (page < 1) 0 else page - 1,
                PAGE_SIZE,
                Sort.Direction.DESC,
                "createdAt",
            )
        val pageOfPosts: Page<PostDTO> =
            postRepository
                .findByTitleContainingIgnoreCase(query, pageable)
                .map(PostDTO::from)
        return PagedResult(
            pageOfPosts.content,
            pageOfPosts.totalElements,
            pageOfPosts.number + 1,
            pageOfPosts.totalPages,
        )
    }

    @Transactional(readOnly = true)
    fun getPostById(id: Long): Optional<PostDTO> {
        log.debug { "get post by id=$id" }
        return postRepository.findById(id).map(PostDTO::from)
    }

    fun createPost(createPostRequest: CreatePostRequest): PostDTO {
        val post = Post()
        post.url = createPostRequest.url
        post.title = createPostRequest.title
        post.content = createPostRequest.content
        post.createdBy = userRepository.getReferenceById(createPostRequest.userId)
        log.debug { "create post with url=${post.url}" }
        val savedPost = postRepository.save(post)
        return PostDTO.from(savedPost)
    }

    fun deletePost(id: Long) {
        log.debug { "delete post by id=$id" }
        postRepository.deleteById(id)
    }

    fun deleteAllPosts() {
        log.debug { "delete all posts" }
        postRepository.deleteAllInBatch()
    }
}
