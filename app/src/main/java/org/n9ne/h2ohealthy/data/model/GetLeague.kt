package org.n9ne.h2ohealthy.data.model

data class GetLeague(
    val name: String,
    val code: String,
    val adminEmail: String,
    val members: List<Member>
)
