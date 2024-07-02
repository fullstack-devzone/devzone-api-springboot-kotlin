package com.sivalabs.devzone.posts.domain

import jakarta.validation.constraints.NotBlank

data class CreatePostCmd(
    @field:NotBlank(message = "URL cannot be blank")
    val url: String,
    @field:NotBlank(message = "Title cannot be blank")
    val title: String,
    val content: String?,
    val userId: Long,
)
