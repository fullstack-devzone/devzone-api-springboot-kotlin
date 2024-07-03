package com.sivalabs.devzone.posts

import com.sivalabs.devzone.ApplicationProperties
import com.sivalabs.devzone.posts.domain.PostImportService
import com.sivalabs.devzone.posts.domain.PostService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class DataInitializer(
    private val postService: PostService,
    private val postImportService: PostImportService,
    private val properties: ApplicationProperties,
) : CommandLineRunner {
    private val log = KotlinLogging.logger {}

    override fun run(vararg args: String) {
        if (properties.importDataEnabled) {
            postService.deleteAllPosts()
            val fileName = properties.importFilePath
            postImportService.importPosts(fileName)
        } else {
            log.info { "Data importing is disabled" }
        }
    }
}
