package com.sivalabs.devzone.posts.api

import com.sivalabs.devzone.BaseIT
import com.sivalabs.devzone.TestConstants.ADMIN_EMAIL
import com.sivalabs.devzone.TestConstants.NORMAL_USER_EMAIL
import com.sivalabs.devzone.posts.domain.CreatePostRequest
import com.sivalabs.devzone.posts.domain.PostImportService
import com.sivalabs.devzone.posts.domain.PostService
import com.sivalabs.devzone.security.TokenHelper
import com.sivalabs.devzone.users.domain.UserService
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired

class PostControllerTests : BaseIT() {
    @Autowired
    lateinit var postService: PostService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var postImportService: PostImportService

    @Autowired
    lateinit var tokenHelper: TokenHelper

    @BeforeEach
    fun setUp() {
        postService.deleteAllPosts()
        postImportService.importPosts("/data/test-posts.csv")
    }

    @ParameterizedTest
    @CsvSource("1,25,3,1,true,false,true,false", "2,25,3,2,false,false,true,true", "3,25,3,3,false,true,false,true")
    fun `should get Posts by page number`(
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
    @CsvSource("spring,1,25,3,1,true,false,true,false")
    fun `should search Posts by keyword`(
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
    fun `should create Post successfully given valid data`() {
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
    fun `should fail to create Post when Url is not present`() {
        val jwtToken = tokenHelper.generateToken(ADMIN_EMAIL)
        given().contentType(ContentType.JSON)
            .header(properties.jwt.header, "Bearer $jwtToken")
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
    fun `should get Post by Id successfully`() {
        val user = userService.getUserByEmail(NORMAL_USER_EMAIL)!!
        val request = CreatePostRequest("Sample title", "https://sivalabs.in", "Sample content", user.id!!)
        val postId = postService.createPost(request)
        given().contentType("application/json").get("/api/posts/{id}", postId)
            .then()
            .statusCode(200)
            .body("id", CoreMatchers.equalTo(postId.toInt()))
            .body("title", CoreMatchers.equalTo(request.title))
            .body("url", CoreMatchers.equalTo(request.url))
            .body("createdBy.id", CoreMatchers.equalTo(user.id?.toInt()))
    }

    @Test
    fun `should get NotFound when Post Id not exist`() {
        given().contentType("application/json").get("/api/posts/{id}", 9999)
            .then()
            .statusCode(404)
    }

    @Test
    fun `should be able to delete own Posts`() {
        val user = userService.getUserByEmail(NORMAL_USER_EMAIL)!!
        val jwtToken = tokenHelper.generateToken(NORMAL_USER_EMAIL)
        val request = CreatePostRequest("Sample title", "https://sivalabs.in", "Sample content", user.id!!)
        val postId = postService.createPost(request)
        given().contentType("application/json")
            .header(properties.jwt.header, "Bearer $jwtToken")
            .delete("/api/posts/{id}", postId)
            .then()
            .statusCode(200)
    }

    @Test
    fun `should get NotFound when PostId not exist to delete`() {
        val jwtToken = tokenHelper.generateToken(ADMIN_EMAIL)
        given().contentType("application/json")
            .header(properties.jwt.header, "Bearer $jwtToken")
            .delete("/api/posts/{id}", 9999)
            .then()
            .statusCode(404)
    }

    @Test
    fun `Admin should be able to delete Post created by other users`() {
        val user = userService.getUserByEmail(NORMAL_USER_EMAIL)!!
        val jwtToken = tokenHelper.generateToken(ADMIN_EMAIL)
        val request = CreatePostRequest("Sample title", "https://sivalabs.in", "Sample content", user.id!!)
        val postId = postService.createPost(request)
        given().contentType("application/json")
            .header(properties.jwt.header, "Bearer $jwtToken")
            .delete("/api/posts/{id}", postId)
            .then()
            .statusCode(200)
    }

    @Test
    fun `Normal user should not be able to delete Post created by other users`() {
        val user = userService.getUserByEmail(ADMIN_EMAIL)!!
        val jwtToken = tokenHelper.generateToken(NORMAL_USER_EMAIL)
        val request = CreatePostRequest("Sample title", "https://sivalabs.in", "Sample content", user.id!!)
        val postId = postService.createPost(request)
        given().contentType("application/json")
            .header(properties.jwt.header, "Bearer $jwtToken")
            .delete("/api/posts/{id}", postId)
            .then()
            .statusCode(403)
    }
}
