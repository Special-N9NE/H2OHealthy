package org.n9ne.h2ohealthy.data.repo.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActivityEntity(
    @ColumnInfo val type: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}