package com.sivalabs.devzone.posts.domain

import com.opencsv.CSVIterator
import com.opencsv.CSVReader
import com.sivalabs.devzone.users.domain.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

@Service
@Transactional
class PostImportService(
    private val postService: PostService,
    private val userService: UserService,
) {
    private val log = KotlinLogging.logger {}

    companion object {
        const val SYSTEM_USER_EMAIL = "admin@gmail.com"
    }

    fun importPosts(fileName: String) {
        log.info { "Importing posts from file: $fileName" }
        var count = 0L

        val file = ClassPathResource(fileName, this.javaClass)
        val inputStreamReader = InputStreamReader(file.inputStream, StandardCharsets.UTF_8)
        val csvReader = CSVReader(inputStreamReader)
        csvReader.skip(1)
        val iterator = CSVIterator(csvReader)

        val user = userService.getUserByEmail(SYSTEM_USER_EMAIL) ?: throw IllegalArgumentException()
        while (iterator.hasNext()) {
            val nextLine = iterator.next()
            val request =
                CreatePostRequest(
                    nextLine[1],
                    nextLine[0],
                    nextLine[1],
                    user.id!!,
                )
            postService.createPost(request)
            count++
        }

        log.info { "Imported $count posts from file $fileName" }
    }
}
