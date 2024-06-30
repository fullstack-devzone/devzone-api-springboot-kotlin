package com.sivalabs.devzone.posts.domain

import java.time.LocalDateTime

data class Post(
    var id: Long?,
    var url: String,
    var title: String,
    var content: String?,
    var createdBy: Long,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime? = null,
)
