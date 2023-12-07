package org.n9ne.h2ohealthy.data.model

data class League(
    var id: Long?,
    val idUser: Long?,
    val adminEmail: String?,
    var name: String,
    var code: String
)
