package com.sivalabs.devzone.posts.api

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException
import com.sivalabs.devzone.common.exceptions.UnauthorisedAccessException
import com.sivalabs.devzone.common.models.PagedResult
import com.sivalabs.devzone.posts.domain.CreatePostRequest
import com.sivalabs.devzone.posts.domain.PostDTO
import com.sivalabs.devzone.posts.domain.PostService
import com.sivalabs.devzone.security.SecurityUtils
import com.sivalabs.devzone.users.domain.User
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService,
    private val securityUtils: SecurityUtils,
) {
    private val log = KotlinLogging.logger {}

    @GetMapping
    fun getPosts(
        @RequestParam(name = "query", defaultValue = "") query: String,
        @RequestParam(name = "page", defaultValue = "1") page: Int,
    ): PagedResult<PostDTO> {
        return if (StringUtils.isNotEmpty(query)) {
            log.info { "Searching posts for $query with page: $page" }
            postService.searchPosts(query, page)
        } else {
            log.info { "Fetching posts with page: $page" }
            postService.getAllPosts(page)
        }
    }

    @GetMapping("/{id}")
    fun getPost(
        @PathVariable id: Long,
    ): PostDTO? {
        return postService.getPostById(id) ?: throw ResourceNotFoundException("Post with id: $id not found")
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Post", security = [SecurityRequirement(name = "bearerAuth")])
    fun createPost(
        @RequestBody @Valid createPostRequest: CreatePostRequest,
    ): PostDTO? {
        val loginUser = securityUtils.loginUser()!!
        val request =
            CreatePostRequest(
                createPostRequest.title,
                createPostRequest.url,
                createPostRequest.content,
                loginUser.id!!,
            )
        val id = postService.createPost(request)
        return postService.getPostById(id)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Post", security = [SecurityRequirement(name = "bearerAuth")])
    fun deletePost(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        val loginUser = securityUtils.loginUser()!!
        val post = postService.getPostById(id) ?: throw ResourceNotFoundException("Post with id: $id not found")
        checkPrivilege(post, loginUser)
        postService.deletePost(id)
        return ResponseEntity.ok().build()
    }

    private fun checkPrivilege(
        post: PostDTO,
        loginUser: User,
    ) {
        if (!(post.createdBy?.id == loginUser.id || loginUser.isCurrentUserAdmin())) {
            throw UnauthorisedAccessException("Unauthorised Access")
        }
    }
}
