package org.n9ne.h2ohealthy.data.repo.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @ColumnInfo val idActivity: Long,
    @ColumnInfo val idLeague: Long,
    @ColumnInfo val email: String,
    @ColumnInfo val password: String,
    @ColumnInfo val date: String,
    @ColumnInfo val name: String,
    @ColumnInfo val birthdate: String,
    @ColumnInfo val weight: Int,
    @ColumnInfo val height: Int,
    @ColumnInfo val gender: Int,
    @ColumnInfo val score: Int,
    @ColumnInfo val target: String,
    @ColumnInfo val profile: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}