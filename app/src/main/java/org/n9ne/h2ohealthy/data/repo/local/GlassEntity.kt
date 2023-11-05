package org.n9ne.h2ohealthy.data.repo.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GlassEntity(
    @ColumnInfo val idUser: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val capacity: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}