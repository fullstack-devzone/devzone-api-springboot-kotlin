package com.sivalabs.devzone.posts.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class PostDTO(
    var id: Long? = null,
    var url: String = "",
    var title: String = "",
    var content: String = "",
    var createdBy: PostCreator? = null,
    @JsonProperty("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @JsonProperty("updated_at")
    var updatedAt: LocalDateTime? = null,
)

data class PostCreator(var id: Long, var name: String)
