package com.sivalabs.devzone.links.web.controllers

import com.sivalabs.devzone.links.entities.Tag
import com.sivalabs.devzone.links.services.LinkService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tags")
class TagController(
    private val linkService: LinkService
) {
    @GetMapping
    fun allTags(): List<Tag> {
        return linkService.findAllTags()
    }
}
