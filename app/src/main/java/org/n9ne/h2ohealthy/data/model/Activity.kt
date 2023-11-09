package org.n9ne.h2ohealthy.data.model

data class Activity(
    val id: Long,
    val idUser: Long,
    var amount: String,
    val date: String,
    val time: String
)
