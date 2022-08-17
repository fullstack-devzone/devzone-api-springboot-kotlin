package com.sivalabs.devzone.links.mappers

import com.sivalabs.devzone.links.entities.Link
import com.sivalabs.devzone.links.models.LinkDTO
import org.springframework.stereotype.Component

@Component
class LinkMapper {
    fun toDTO(link: Link): LinkDTO {
        val dto = LinkDTO()
        dto.id = link.id
        dto.url = link.url
        dto.title = link.title
        dto.createdUserId = link.createdBy!!.id
        dto.createdUserName = link.createdBy!!.name
        dto.createdAt = link.createdAt
        dto.updatedAt = link.updatedAt
        dto.tags = link.tags.map { it.name }.toMutableList()
        return dto
    }
}
