package com.sivalabs.devzone.links.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.domain.Page

class LinksDTO(bookmarksPage: Page<LinkDTO>) {
    var data: MutableList<LinkDTO> = mutableListOf()
    var totalElements: Long = 0
    var pageNumber = 0
    var totalPages = 0

    @JsonProperty("isFirst")
    var isFirst = false

    @JsonProperty("isLast")
    var isLast = false

    @JsonProperty("hasNext")
    var hasNext = false

    @JsonProperty("hasPrevious")
    var hasPrevious = false

    init {
        this.data = (bookmarksPage.content)
        this.totalElements = (bookmarksPage.totalElements)
        this.pageNumber = (bookmarksPage.number + 1) // 1 - based page numbering
        this.totalPages = (bookmarksPage.totalPages)
        this.isFirst = (bookmarksPage.isFirst)
        this.isLast = (bookmarksPage.isLast)
        this.hasNext = (bookmarksPage.hasNext())
        this.hasPrevious = (bookmarksPage.hasPrevious())
    }
}
