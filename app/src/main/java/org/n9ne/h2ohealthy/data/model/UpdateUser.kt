package org.n9ne.h2ohealthy.data.model

data class UpdateUser(
    var idActivity: String,
    val email: String,
    val name: String,
    val birthdate: String,
    val weight: String,
    val height: String,
    var gender: String,
)
