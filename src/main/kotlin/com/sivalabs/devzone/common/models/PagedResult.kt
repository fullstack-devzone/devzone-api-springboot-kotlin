package com.sivalabs.devzone.common.models

import com.fasterxml.jackson.annotation.JsonProperty

data class PagedResult<T> (
    val data: List<T>,
    val totalElements: Long,
    val pageNumber: Int,
    val totalPages: Int,
    @field:JsonProperty("isFirst") val isFirst: Boolean,
    @field:JsonProperty("isLast") val isLast: Boolean,
    @field:JsonProperty("hasNext") val hasNext: Boolean,
    @field:JsonProperty("hasPrevious") val hasPrevious: Boolean,
) {
    constructor(data: List<T>, totalElements: Long, pageNumber: Int, totalPages: Int) :
        this(
            data,
            totalElements,
            pageNumber,
            totalPages,
            pageNumber == 1,
            pageNumber == totalPages,
            (pageNumber < totalPages),
            pageNumber > 1,
        )
}
