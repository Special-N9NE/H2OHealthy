package org.n9ne.h2ohealthy.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GlassEntity(
    @ColumnInfo @PrimaryKey val id: Long,
    @ColumnInfo val idUser: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val capacity: String,
    @ColumnInfo val color: String
)