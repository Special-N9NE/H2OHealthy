package org.n9ne.h2ohealthy.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WaterEntity(
    @ColumnInfo @PrimaryKey val id: Long,
    @ColumnInfo val idUser: Long?,
    @ColumnInfo val date: String,
    @ColumnInfo val amount: String,
    @ColumnInfo val time: String,
)