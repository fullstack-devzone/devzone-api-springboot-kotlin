package com.sivalabs.devzone.links.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

class LinkDTO {
    var id: Long? = null

    @NotBlank(message = "URL cannot be blank")
    var url: String? = null
    var title: String? = null

    @JsonProperty("created_user_id")
    var createdUserId: Long? = null

    @JsonProperty("created_user_name")
    var createdUserName: String? = null

    @JsonProperty("created_at")
    var createdAt: LocalDateTime? = null

    @JsonProperty("updated_at")
    var updatedAt: LocalDateTime? = null
    var tags: MutableList<String> = mutableListOf()
}
