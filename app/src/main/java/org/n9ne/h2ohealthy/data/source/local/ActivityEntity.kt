package org.n9ne.h2ohealthy.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActivityEntity(
    @ColumnInfo @PrimaryKey val id: Long,
    @ColumnInfo val type: String
)