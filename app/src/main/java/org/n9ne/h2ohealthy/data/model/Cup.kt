package org.n9ne.h2ohealthy.data.model

data class Cup(
    var id : Long?,
    val idUser: Long,
    val title: String,
    val capacity: Int,
    val color: String
)
