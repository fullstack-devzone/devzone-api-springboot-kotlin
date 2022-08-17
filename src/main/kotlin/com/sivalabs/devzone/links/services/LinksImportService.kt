package com.sivalabs.devzone.links.services

import com.opencsv.CSVIterator
import com.opencsv.CSVReader
import com.sivalabs.devzone.common.logging.logger
import com.sivalabs.devzone.links.models.LinkDTO
import com.sivalabs.devzone.users.services.UserService
import org.apache.commons.lang3.StringUtils
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

@Service
@Transactional
class LinksImportService(
    private val linkService: LinkService,
    private val userService: UserService
) {
    companion object {
        private val log = logger()
        const val SYSTEM_USER_EMAIL = "admin@gmail.com"
    }

    fun importBookmarks(fileName: String) {
        log.info("Importing links from file: {}", fileName)
        var count = 0L

        val file = ClassPathResource(fileName, this.javaClass)
        val inputStreamReader = InputStreamReader(file.inputStream, StandardCharsets.UTF_8)
        val csvReader = CSVReader(inputStreamReader)
        csvReader.skip(1)
        val iterator = CSVIterator(csvReader)
        while (iterator.hasNext()) {
            val nextLine = iterator.next()
            val linkDTO = LinkDTO()
            linkDTO.url = nextLine.get(0)
            linkDTO.title = nextLine.get(1)
            linkDTO.createdUserId = userService.getUserByEmail(SYSTEM_USER_EMAIL).orElseThrow().id
            linkDTO.createdAt = LocalDateTime.now()
            if (nextLine.size > 2 && StringUtils.trimToNull(nextLine[2]) != null) {
                linkDTO.tags = StringUtils.trimToEmpty(nextLine[2]).split("\\|".toRegex()).toMutableList()
            }
            linkService.createLink(linkDTO)
            count++
        }

        log.info("Imported {} links from file {}", count, fileName)
    }
}
