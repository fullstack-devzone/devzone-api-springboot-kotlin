package com.sivalabs.devzone.posts

import com.sivalabs.devzone.common.logging.logger
import com.sivalabs.devzone.config.ApplicationProperties
import com.sivalabs.devzone.posts.services.PostImportService
import com.sivalabs.devzone.posts.services.PostService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class DataInitializer(
    private val applicationProperties: ApplicationProperties,
    private val postService: PostService,
    private val postImportService: PostImportService,
) : CommandLineRunner {

    companion object {
        private val log = logger()
    }

    override fun run(vararg args: String) {
        if (applicationProperties.importDataEnabled) {
            postService.deleteAllPosts()
            val fileName = applicationProperties.importFilePath
            postImportService.importPosts(fileName)
        } else {
            log.info("Data importing is disabled")
        }
    }
}
