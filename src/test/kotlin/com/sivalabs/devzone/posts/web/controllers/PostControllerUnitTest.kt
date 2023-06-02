package com.sivalabs.devzone.posts.web.controllers

import com.sivalabs.devzone.common.AbstractWebMvcTest
import com.sivalabs.devzone.common.models.PagedResult
import com.sivalabs.devzone.posts.models.PostDTO
import com.sivalabs.devzone.posts.services.PostService
import com.sivalabs.devzone.users.services.SecurityService
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(controllers = [PostController::class])
internal class PostControllerUnitTest : AbstractWebMvcTest() {
    @MockBean
    lateinit var postService: PostService

    @MockBean
    lateinit var securityService: SecurityService

    @Test
    fun shouldFetchPostsFirstPage() {
        val postsDTO = PagedResult(listOf<PostDTO>(), 10, 1, 1)
        BDDMockito.given(postService.getAllPosts(ArgumentMatchers.any(Int::class.java))).willReturn(postsDTO)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts")).andExpect(MockMvcResultMatchers.status().isOk())
    }
}
