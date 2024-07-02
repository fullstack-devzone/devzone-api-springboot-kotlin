package com.sivalabs.devzone.users.domain

class User {
    var id: Long? = null
    var name: String = ""
    var email: String = ""
    var password: String = ""
    var role: RoleEnum = RoleEnum.ROLE_USER
}
