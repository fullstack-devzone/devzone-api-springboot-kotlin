package com.sivalabs.devzone.users.domain

import java.util.stream.Collectors

enum class RoleEnum {
    ROLE_ADMIN,
    ROLE_MODERATOR,
    ROLE_USER,
    ;

    companion object {
        fun getRoleHierarchy(): String {
            val hierarchy = listOf(ROLE_ADMIN, ROLE_MODERATOR, ROLE_USER)
            return hierarchy.stream().map { obj: RoleEnum -> obj.name }
                .collect(Collectors.joining(" > "))
        }
    }
}
