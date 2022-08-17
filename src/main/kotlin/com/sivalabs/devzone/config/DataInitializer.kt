package com.sivalabs.devzone.config

import com.sivalabs.devzone.common.logging.logger
import com.sivalabs.devzone.links.services.LinkService
import com.sivalabs.devzone.links.services.LinksImportService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
@Transactional
class DataInitializer(
    private val applicationProperties: ApplicationProperties,
    private val linkService: LinkService,
    private val linksImportService: LinksImportService,
    private val messageSource: MessageSource
) : CommandLineRunner {

    companion object {
        private val log = logger()
    }

    override fun run(vararg args: String) {
        log.info(
            messageSource.getMessage(
                "startup-message",
                null,
                "Welcome to DevZone!!!",
                Locale.getDefault()
            )
        )
        if (applicationProperties.importDataEnabled) {
            linkService.deleteAllLinks()
            val fileName = applicationProperties.importFilePath
            linksImportService.importBookmarks(fileName)
        } else {
            log.info("Data importing is disabled")
        }
    }
}
