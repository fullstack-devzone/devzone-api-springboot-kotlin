package com.sivalabs.devzone.posts.domain

import java.time.LocalDateTime

data class PostDTO(
    var id: Long? = null,
    var url: String = "",
    var title: String = "",
    var content: String = "",
    var createdBy: PostCreator? = null,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime? = null,
)

data class PostCreator(var id: Long, var name: String)
