package org.n9ne.common.model

data class Cup(
    var id : Long?,
    val idUser: Long?,
    var title: String,
    var capacity: Int,
    var color: String
)
