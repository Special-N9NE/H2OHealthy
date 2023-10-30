package org.n9ne.h2ohealthy.data.model

data class User(
    val id: String,
    var name : String,
    var weight: String,
    var height: String,
    var birthday : String,
    var score: Int
)
