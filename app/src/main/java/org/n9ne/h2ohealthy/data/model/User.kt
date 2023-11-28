package org.n9ne.h2ohealthy.data.model

data class User(
    val id: Long,
    val activityType: ActivityType,
    val idLeague: Long,
    val email: String,
    val name: String,
    var age: String,
    val birthDate: String,
    val weight: String,
    val height: String,
    val gender: String,
    val score: Int,
    val profile: String
)
