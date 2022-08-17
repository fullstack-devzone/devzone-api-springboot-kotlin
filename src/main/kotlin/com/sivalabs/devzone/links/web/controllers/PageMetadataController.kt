package com.sivalabs.devzone.links.web.controllers

import com.sivalabs.devzone.common.logging.logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

@RestController
class PageMetadataController {
    companion object {
        private val log = logger()
    }

    @GetMapping("/api/page-metadata")
    fun getPageMetadata(@RequestParam url: String): MutableMap<String, String> {
        val metadata: MutableMap<String, String> = ConcurrentHashMap()
        try {
            val doc = org.jsoup.Jsoup.connect(url).get()
            metadata["title"] = doc.title()
        } catch (e: IOException) {
            log.error(e.message, e)
            metadata["title"] = url
        }
        return metadata
    }
}
