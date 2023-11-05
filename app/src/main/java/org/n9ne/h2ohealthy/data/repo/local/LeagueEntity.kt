package org.n9ne.h2ohealthy.data.repo.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LeagueEntity(
    @ColumnInfo val idUser: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val code: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}