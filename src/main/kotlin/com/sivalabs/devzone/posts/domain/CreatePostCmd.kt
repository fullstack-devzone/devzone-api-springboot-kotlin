package com.sivalabs.devzone.posts.domain

data class CreatePostCmd(
    val url: String,
    val title: String,
    val content: String?,
    val userId: Long,
)
