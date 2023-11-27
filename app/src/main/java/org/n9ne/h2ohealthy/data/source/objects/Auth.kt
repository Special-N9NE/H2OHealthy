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

    data class CompleteProfile(
        val email: String,
        val name: String,
        val password: String,

        val date: String,

        val idActivity: String,
        val birthdate: String,
        val weight: String,
        val height: String,
        val gender: String,

        val target: String = "3",
        val idLeague: String = "0",
        val score: String = "0"
    )
}