package org.n9ne.h2ohealthy.data.source.objects

sealed class Auth {
    data class Login(
        val email: String,
        val password: String
    )

    data class Register(
        val name: String,
        val email: String,
        val password: String
    )
}