package org.n9ne.h2ohealthy.data.source.objects

data class GetMembers(
    val members: List<Data>?,
    val name: String?,
    val admin: String?,
    val code: String?,
    val status: Boolean,
    val message: String?
) {
    data class Data(
        val id: Long,
        val name: String,
        val profile: String,
        val score: Long
    )
}