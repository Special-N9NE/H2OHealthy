package org.n9ne.h2ohealthy.data.source.objects

data class Login(
    val message: String,
    val status: Boolean,
    val user: List<User>?
) {
    data class User(
        val birthdate: String,
        val date: String,
        val email: String,
        val gender: Int,
        val height: Int,
        val id: Int,
        val idActivity: Int,
        val idleague: Int,
        val name: String,
        val password: String,
        val profile: String,
        val score: Int,
        val target: String,
        val weight: Int
    )
}