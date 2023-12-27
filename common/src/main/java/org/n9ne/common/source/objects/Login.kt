package org.n9ne.common.source.objects

data class Login(
    val message: String,
    val status: Boolean,
    val user: List<GetUser.User>?
)