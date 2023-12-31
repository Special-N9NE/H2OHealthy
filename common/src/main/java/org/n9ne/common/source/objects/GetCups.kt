package org.n9ne.common.source.objects

data class GetCups(
    val data: List<Cup>?,
    val status: Boolean,
    val message: String?
) {
    data class Cup(
        val capacity: String,
        val color: String,
        val id: Int,
        val idUser: Int,
        val name: String
    )
}