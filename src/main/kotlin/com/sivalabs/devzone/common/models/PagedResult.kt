package com.sivalabs.devzone.common.models

data class PagedResult<T>(
    val data: List<T>,
    val totalElements: Long,
    val pageNumber: Int,
    val totalPages: Int,
    val isFirst: Boolean,
    val isLast: Boolean,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
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
