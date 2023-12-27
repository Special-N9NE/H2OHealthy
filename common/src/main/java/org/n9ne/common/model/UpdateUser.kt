package org.n9ne.common.model

data class UpdateUser(
    var idActivity: String,
    val email: String,
    val name: String,
    val birthdate: String,
    val weight: String,
    val height: String,
    var gender: String,
)
