package org.n9ne.h2ohealthy.data.model

data class Cup(
    var id : Long?,
    val idUser: Long,
    var title: String,
    var capacity: Int,
    var color: String
)
