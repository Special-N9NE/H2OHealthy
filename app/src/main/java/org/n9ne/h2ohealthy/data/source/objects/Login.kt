package org.n9ne.h2ohealthy.data.source.objects

sealed class Login {
    data class Send(
        val email: String,
        val password: String
    )
}