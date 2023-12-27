package org.n9ne.common.model

data class LoginResult(
    val user: User,
    val token: String
)
