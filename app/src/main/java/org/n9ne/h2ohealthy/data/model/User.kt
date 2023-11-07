package org.n9ne.h2ohealthy.data.model

data class User(
    val id: Long,
    val activityType: ActivityType,
    val idLeague: Long,
    val email: String,
    val name: String,
    val age: String,
    val weight: Int,
    val height: Int,
    val score: Int,
    val profile: String
)
