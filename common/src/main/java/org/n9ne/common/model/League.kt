package org.n9ne.common.model

data class League(
    var id: Long?,
    val idUser: Long?,
    val adminEmail: String?,
    var name: String,
    var code: String
)
