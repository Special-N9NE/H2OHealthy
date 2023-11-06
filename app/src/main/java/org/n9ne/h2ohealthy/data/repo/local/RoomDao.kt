package org.n9ne.h2ohealthy.data.repo.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM UserEntity")
    fun getUser(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWater(water: WaterEntity): Long
    @Query("SELECT * FROM WaterEntity")
    fun getProgress(): List<WaterEntity>

}