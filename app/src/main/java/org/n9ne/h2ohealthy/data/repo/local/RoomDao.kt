package org.n9ne.h2ohealthy.data.repo.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.n9ne.h2ohealthy.data.model.Cup

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

    @Query("UPDATE WaterEntity SET amount = :amount WHERE id = :id")
    fun updateWater(id: Long, amount: String)

    @Query("DELETE FROM WaterEntity WHERE id = :id")
    fun removeWater(id: Long)

    @Query("SELECT * FROM GlassEntity")
    fun getCups(): List<GlassEntity>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCup(cup: GlassEntity): Long
    @Query("UPDATE GlassEntity SET name = :title , capacity = :capacity , color = :color WHERE id = :id")
    fun updateCup(id: Long, title : String , capacity : String , color: String)
}