package com.sivalabs.devzone.posts.web.controllers

import com.opencsv.exceptions.CsvValidationException
import com.sivalabs.devzone.common.AbstractIntegrationTest
import com.sivalabs.devzone.common.TestConstants.ADMIN_EMAIL
import com.sivalabs.devzone.common.TestConstants.NORMAL_USER_EMAIL
import com.sivalabs.devzone.config.security.TokenHelper
import com.sivalabs.devzone.posts.models.CreatePostRequest
import com.sivalabs.devzone.posts.services.PostImportService
import com.sivalabs.devzone.posts.services.PostService
import com.sivalabs.devzone.users.services.UserService
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import java.io.IOException

internal class PostControllerTests : AbstractIntegrationTest() {
    @Autowired
    lateinit var postService: PostService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var postImportService: PostImportService

    @Autowired
    lateinit var tokenHelper: TokenHelper

    @BeforeEach
    @Throws(CsvValidationException::class, IOException::class)
    fun setUp() {
        postService.deleteAllPosts()
        postImportService.importPosts("/data/test-posts.csv")
    }

    @ParameterizedTest
    @CsvSource("1,25,3,1,true,false,true,false", "2,25,3,2,false,false,true,true", "3,25,3,3,false,true,false,true")
    fun shouldFetchPostsByPageNumber(
        pageNo: Int,
        totalElements: Int,
        totalPages: Int,
        pageNumber: Int,
        isFirst: Boolean,
        isLast: Boolean,
        hasNext: Boolean,
        hasPrevious: Boolean,
    ) {
        given().get("/api/posts?page={page}", pageNo)
            .then()
            .statusCode(200)
            .body("totalElements", CoreMatchers.equalTo(totalElements))
            .body("totalPages", CoreMatchers.equalTo(totalPages))
            .body("pageNumber", CoreMatchers.equalTo(pageNumber))
            .body("isFirst", CoreMatchers.equalTo(isFirst))
            .body("isLast", CoreMatchers.equalTo(isLast))
            .body("hasNext", CoreMatchers.equalTo(hasNext))
            .body("hasPrevious", CoreMatchers.equalTo(hasPrevious))
    }

    @ParameterizedTest
    @CsvSource("spring,1,9,1,1,true,true,false,false")
    fun shouldSearchPosts(
        query: String?,
        pageNo: Int,
        totalElements: Int,
        totalPages: Int,
        pageNumber: Int,
        isFirst: Boolean,
        isLast: Boolean,
        hasNext: Boolean,
        hasPrevious: Boolean,
    ) {
        given().get("/api/posts?query={query}&page={page}", query, pageNo)
            .then()
            .statusCode(200)
            .body("totalElements", CoreMatchers.equalTo(totalElements))
            .body("totalPages", CoreMatchers.equalTo(totalPages))
            .body("pageNumber", CoreMatchers.equalTo(pageNumber))
            .body("isFirst", CoreMatchers.equalTo(isFirst))
            .body("isLast", CoreMatchers.equalTo(isLast))
            .body("hasNext", CoreMatchers.equalTo(hasNext))
            .body("hasPrevious", CoreMatchers.equalTo(hasPrevious))
    }

    @Test
    fun shouldCreatePostSuccessfully() {
        val jwtToken = tokenHelper.generateToken(ADMIN_EMAIL)
        given().contentType("application/json")
            .header(properties.jwt.header, "Bearer $jwtToken")
            .body(
                """
                {
                    "title": "SivaLabs Blog",
                    "url": "https://sivalabs.in",
                    "content": "java blog"
                }
                
                """.trimIndent(),
            )
            .post("/api/posts")
            .then()
            .statusCode(201)
            .body("id", Matchers.notNullValue())
            .body("title", Matchers.`is`("SivaLabs Blog"))
            .body("url", Matchers.`is`("https://sivalabs.in"))
    }

    @Test
    fun shouldFailToCreatePostWhenUrlIsNotPresent() {
        given().contentType(ContentType.JSON)
            .body(
                """
                {
                    "title": "SivaLabs Blog"
                }
                
                """.trimIndent(),
            )
            .post("/api/posts")
            .then()
            .statusCode(400)
    }

    @Test
    fun shouldGetPostByIdSuccessfully() {
        val user = userService.getUserByEmail(NORMAL_USER_EMAIL).orElseThrow()
        val request = CreatePostRequest("Sample title", "https://sivalabs.in", "Sample content", user.id!!)
        val post = postService.createPost(request)
        given().contentType("application/json").get("/api/posts/{id}", post.id)
            .then()
            .statusCode(200)
            .body("id", CoreMatchers.equalTo(post.id!!.toInt()))
            .body("title", CoreMatchers.equalTo(post.title))
            .body("url", CoreMatchers.equalTo(post.url))
            .body("createdBy.id", CoreMatchers.equalTo(user.id!!.toInt()))
    }

    @Test
    fun shouldGetNotFoundWhenPostIdNotExist() {
        given().contentType("application/json").get("/api/posts/{id}", 9999)
            .then()
            .statusCode(404)
    }

    @Test
    fun shouldBeAbleToDeleteOwnPosts() {
        val user = userService.getUserByEmail(NORMAL_USER_EMAIL).orElseThrow()
        val jwtToken = tokenHelper.generateToken(NORMAL_USER_EMAIL)
        val request = CreatePostRequest("Sample title", "https://sivalabs.in", "Sample content", user.id!!)
        val post = postService.createPost(request)
        given().contentType("application/json")
            .header(properties.jwt.header, "Bearer $jwtToken")
            .delete("/api/posts/{id}", post.id)
            .then()
            .statusCode(200)
    }

    @Test
    fun shouldGetNotFoundWhenPostIdNotExistToDelete() {
        val jwtToken = tokenHelper.generateToken(ADMIN_EMAIL)
        given().contentType("application/json")
            .header(properties.jwt.header, "Bearer $jwtToken")
            .delete("/api/posts/{id}", 9999)
            .then()
            .statusCode(404)
    }

    @Test
    fun adminShouldBeAbleToDeletePostCreatedByOtherUsers() {
        val user = userService.getUserByEmail(NORMAL_USER_EMAIL).orElseThrow()
        val jwtToken = tokenHelper.generateToken(ADMIN_EMAIL)
        val request = CreatePostRequest("Sample title", "https://sivalabs.in", "Sample content", user.id!!)
        val post = postService.createPost(request)
        given().contentType("application/json")
            .header(properties.jwt.header, "Bearer $jwtToken")
            .delete("/api/posts/{id}", post.id)
            .then()
            .statusCode(200)
    }

    @Test
    fun normalUserShouldNotBeAbleToDeletePostCreatedByOtherUsers() {
        val user = userService.getUserByEmail(ADMIN_EMAIL).orElseThrow()
        val jwtToken = tokenHelper.generateToken(NORMAL_USER_EMAIL)
        val request = CreatePostRequest("Sample title", "https://sivalabs.in", "Sample content", user.id!!)
        val post = postService.createPost(request)
        given().contentType("application/json")
            .header(properties.jwt.header, "Bearer $jwtToken")
            .delete("/api/posts/{id}", post.id)
            .then()
            .statusCode(403)
    }
}
