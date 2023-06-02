package com.sivalabs.devzone.posts.repositories

import com.sivalabs.devzone.common.AbstractRepositoryTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PostRepositoryTests : AbstractRepositoryTest() {
    @Autowired
    private lateinit var postRepository: PostRepository

    @Test
    fun shouldReturnAllPosts() {
        val allPosts = postRepository.findAll()
        Assertions.assertThat(allPosts).isNotNull()
    }
}
