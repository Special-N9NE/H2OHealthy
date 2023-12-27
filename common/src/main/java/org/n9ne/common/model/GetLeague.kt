package org.n9ne.common.model

data class GetLeague(
    val name: String,
    val code: String,
    val adminEmail: String,
    val members: List<Member>
)
