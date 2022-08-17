package com.sivalabs.devzone.links.web.controllers

import com.sivalabs.devzone.common.annotations.AnyAuthenticatedUser
import com.sivalabs.devzone.common.annotations.CurrentUser
import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException
import com.sivalabs.devzone.common.logging.logger
import com.sivalabs.devzone.links.models.LinkDTO
import com.sivalabs.devzone.links.models.LinksDTO
import com.sivalabs.devzone.links.services.LinkService
import com.sivalabs.devzone.users.models.UserDTO
import com.sivalabs.devzone.users.services.SecurityService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class LinkController(
    private val linkService: LinkService,
    private val securityService: SecurityService
) {
    companion object {
        private val log = logger()
    }

    @GetMapping("/links")
    fun getLinks(
        @RequestParam(name = "tag", defaultValue = "") tag: String?,
        @RequestParam(name = "query", defaultValue = "") query: String?,
        @RequestParam(name = "page", defaultValue = "1") page: Int?
    ): LinksDTO? {
        return if (StringUtils.isNotEmpty(query)) {
            log.info("Searching links for {} with page: {}", query, page)
            linkService.searchLinks(query!!, page!!)
        } else if (StringUtils.isNotEmpty(tag)) {
            log.info("Fetching links for tag {} with page: {}", tag, page)
            linkService.getLinksByTag(tag!!, page!!)
        } else {
            log.info("Fetching links with page: {}", page)
            linkService.getAllLinks(page!!)
        }
    }

    @GetMapping("/links/{id}")
    fun getLink(@PathVariable id: Long): LinkDTO {
        return linkService
            .getLinkById(id)
            .orElseThrow { ResourceNotFoundException("Link with id: $id not found") }
    }

    @PostMapping("/links")
    @ResponseStatus(HttpStatus.CREATED)
    @AnyAuthenticatedUser
    @Operation(summary = "Create Link", security = [SecurityRequirement(name = "bearerAuth")])
    fun createLink(
        @RequestBody @Valid
        link: LinkDTO,
        @CurrentUser loginUser: UserDTO
    ): LinkDTO {
        link.createdUserId = loginUser.id
        return linkService.createLink(link)
    }

    @DeleteMapping("/links/{id}")
    @AnyAuthenticatedUser
    @Operation(summary = "Delete Link", security = [SecurityRequirement(name = "bearerAuth")])
    fun deleteLink(@PathVariable id: Long, @CurrentUser loginUser: UserDTO): ResponseEntity<Void> {
        val link = linkService.getLinkById(id).orElse(null)
        checkPrivilege(id, link, loginUser)
        linkService.deleteLink(id)
        return ResponseEntity.ok().build()
    }

    private fun checkPrivilege(linkId: Long, link: LinkDTO?, loginUser: UserDTO) {
        if (link == null ||
            !(link.createdUserId == loginUser.id || securityService.isCurrentUserAdmin())
        ) {
            throw ResourceNotFoundException("Link not found with id=$linkId")
        }
    }
}
