package org.n9ne.common.model

data class Activity(
    var id: Long?,
    val idUser: Long?,
    var amount: String,
    val date: String,
    val time: String
)
