package org.n9ne.h2ohealthy.data.source.objects

data class GetProgress(
    val data: List<Data>?,
    val status: Boolean,
    val message: String?
) {
    data class Data(
        val id: Int,
        val idUser: Int,
        val amout: String,
        val date: String,
        val time: String
    )
}