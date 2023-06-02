package com.sivalabs.devzone.posts.models

import jakarta.validation.constraints.NotBlank

data class CreatePostRequest(
    val title:
    @NotBlank(message = "URL cannot be blank")
    String,
    val url:
    @NotBlank(message = "URL cannot be blank")
    String,
    val content: String?,
    val userId: Long,
)
