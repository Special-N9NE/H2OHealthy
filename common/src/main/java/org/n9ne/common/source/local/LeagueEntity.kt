package org.n9ne.common.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LeagueEntity(
    @ColumnInfo @PrimaryKey val id: Long,
    @ColumnInfo val idUser: Long?,
    @ColumnInfo val name: String,
    @ColumnInfo val code: String
)