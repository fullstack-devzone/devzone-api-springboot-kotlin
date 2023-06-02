package com.sivalabs.devzone.posts.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.sivalabs.devzone.posts.entities.Post
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

class PostDTO {
    var id: Long? = null

    @NotBlank(message = "URL cannot be blank")
    var url: String? = null
    var title: String? = null
    var content: String? = null

    var createdBy: PostCreator? = null

    @JsonProperty("created_at")
    var createdAt: LocalDateTime? = null

    @JsonProperty("updated_at")
    var updatedAt: LocalDateTime? = null

    companion object {
        fun from(post: Post): PostDTO {
            val dto = PostDTO()
            dto.id = post.id
            dto.url = post.url
            dto.title = post.title
            dto.content = post.content
            dto.createdBy = PostCreator(post.createdBy!!.id, post.createdBy!!.name)

            dto.createdAt = post.createdAt
            dto.updatedAt = post.updatedAt
            return dto
        }
    }

    class PostCreator(var id: Long?, var name: String?)
}
