package org.n9ne.h2ohealthy.data.source.objects

data class Login(
    val message: String,
    val status: Boolean,
    val user: List<GetUser.User>?
)