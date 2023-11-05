package org.n9ne.h2ohealthy.data.repo.objects

sealed class Login {
    data class Send(
        val email: String,
        val password: String
    )
}