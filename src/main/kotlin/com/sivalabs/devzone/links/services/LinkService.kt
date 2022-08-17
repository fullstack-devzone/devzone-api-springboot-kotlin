package com.sivalabs.devzone.links.services

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException
import com.sivalabs.devzone.common.logging.logger
import com.sivalabs.devzone.links.entities.Link
import com.sivalabs.devzone.links.entities.Tag
import com.sivalabs.devzone.links.mappers.LinkMapper
import com.sivalabs.devzone.links.models.LinkDTO
import com.sivalabs.devzone.links.models.LinksDTO
import com.sivalabs.devzone.links.repositories.LinkRepository
import com.sivalabs.devzone.links.repositories.TagRepository
import com.sivalabs.devzone.users.repositories.UserRepository
import org.apache.commons.lang3.StringUtils
import org.jsoup.Jsoup
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.time.LocalDateTime
import java.util.Optional

@Service
@Transactional
class LinkService(
    private val linkRepository: LinkRepository,
    private val tagRepository: TagRepository,
    private val userRepository: UserRepository,
    private val linkMapper: LinkMapper
) {
    companion object {
        private val log = logger()
        const val PAGE_SIZE = 10
    }

    @Transactional(readOnly = true)
    fun getAllLinks(page: Int): LinksDTO? {
        val pageable: Pageable = PageRequest.of(if (page < 1) 0 else page - 1, PAGE_SIZE, Sort.Direction.DESC, "createdAt")
        val pageOfLinkIds: Page<Long> = linkRepository.fetchLinkIds(pageable)
        val links: List<Link> = linkRepository.findLinksWithTags(pageOfLinkIds.content, pageable.sort)
        val pageOfLinks: Page<Link> = PageImpl(links, pageable, pageOfLinkIds.totalElements)
        return buildLinksResult(pageOfLinks)
    }

    @Transactional(readOnly = true)
    fun searchLinks(query: String, page: Int): LinksDTO {
        val pageable: Pageable = PageRequest.of(if (page < 1) 0 else page - 1, PAGE_SIZE, Sort.Direction.DESC, "createdAt")
        val pageOfLinkIds = linkRepository.fetchLinkIdsByTitleContainingIgnoreCase(query, pageable)
        val links = linkRepository.findLinksWithTags(pageOfLinkIds.content, pageable.sort)
        val pageOfLinks: Page<Link> = PageImpl(links, pageable, pageOfLinkIds.totalElements)
        return buildLinksResult(pageOfLinks)
    }

    @Transactional(readOnly = true)
    fun getLinksByTag(tag: String, page: Int): LinksDTO {
        val pageable: Pageable = PageRequest.of(if (page < 1) 0 else page - 1, PAGE_SIZE, Sort.Direction.DESC, "createdAt")
        val tagOptional = tagRepository.findByName(tag)
        if (tagOptional.isEmpty) {
            throw ResourceNotFoundException("Tag $tag not found")
        }
        val pageOfLinkIds = linkRepository.fetchLinkIdsByTag(tag, pageable)
        val links = linkRepository.findLinksWithTags(pageOfLinkIds.content, pageable.sort)
        val pageOfLinks: Page<Link> = PageImpl(links, pageable, pageOfLinkIds.totalElements)
        return buildLinksResult(pageOfLinks)
    }

    @Transactional(readOnly = true)
    fun getLinkById(id: Long): Optional<LinkDTO> {
        log.debug("process=get_link_by_id, id={}", id)
        return linkRepository.findById(id).map { link: Link -> linkMapper.toDTO(link) }
    }

    fun createLink(link: LinkDTO): LinkDTO {
        link.id = null
        log.debug("process=create_link, url={}", link.url)
        return linkMapper.toDTO(saveLink(link))
    }

    fun deleteLink(id: Long?) {
        log.debug("process=delete_link_by_id, id={}", id)
        linkRepository.deleteById(id!!)
    }
    fun deleteAllLinks() {
        log.debug("process=delete_all_links")
        linkRepository.deleteAllInBatch()
    }

    @Transactional(readOnly = true)
    fun findAllTags(): List<Tag> {
        val sort = Sort.by("name")
        return tagRepository.findAll(sort)
    }
    private fun saveLink(linkDTO: LinkDTO): Link {
        var link: Link = Link()
        if (linkDTO.id != null) {
            link = linkRepository.findById(linkDTO.id!!).orElse(link)
        }
        link.url = linkDTO.url
        link.title = getTitle(linkDTO)
        link.createdBy = userRepository.getReferenceById((linkDTO.createdUserId)!!)
        link.createdAt = LocalDateTime.now()
        val tagsList: MutableSet<Tag> = java.util.HashSet<Tag>()
        linkDTO.tags.map { tagName: String ->
            if (tagName.trim { it <= ' ' }.isNotEmpty()) {
                val tag: Tag = createTagIfNotExist(tagName.trim { it <= ' ' })
                tagsList.add(tag)
            }
        }
        link.tags = tagsList
        return linkRepository.save<Link>(link)
    }

    private fun getTitle(link: LinkDTO): String {
        if (StringUtils.isNotEmpty(link.title)) {
            return link.title!!
        }
        try {
            val doc = link.url?.let { Jsoup.connect(it).get() }
            return doc?.title() ?: link.url!!
        } catch (e: IOException) {
            log.error(e.message, e)
        }
        return link.url!!
    }

    private fun createTagIfNotExist(tagName: String): Tag {
        val tagOptional = tagRepository.findByName(tagName)
        if (tagOptional.isPresent) {
            return tagOptional.get()
        }
        val tag = Tag()
        tag.name = tagName
        return tagRepository.save(tag)
    }

    private fun buildLinksResult(links: Page<Link>): LinksDTO {
        log.trace("Found {} links in page", links.numberOfElements)
        return LinksDTO(links.map { link: Link -> linkMapper.toDTO(link) })
    }
}
