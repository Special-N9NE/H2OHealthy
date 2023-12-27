package org.n9ne.common.source.objects

data class GetUser(
    val message: String?,
    val status: Boolean,
    val user: User?
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