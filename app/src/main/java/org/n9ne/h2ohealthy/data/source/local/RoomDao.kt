package org.n9ne.h2ohealthy.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM UserEntity")
    fun getUser(): List<UserEntity>

    @Query("UPDATE UserEntity SET target = :target")
    fun updateTarget(target: Int)
    @Query("UPDATE UserEntity SET score = :score")
    fun updateScore(score: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWater(water: WaterEntity): Long

    @Query("SELECT * FROM WaterEntity")
    fun getProgress(): List<WaterEntity>

    @Query("UPDATE WaterEntity SET amount = :amount WHERE id = :id")
    fun updateWater(id: Long, amount: String)

    @Query("DELETE FROM WaterEntity WHERE id = :id")
    fun removeWater(id: Long)

    @Query("SELECT * FROM GlassEntity")
    fun getCups(): List<GlassEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCup(cup: GlassEntity): Long

    @Query("UPDATE GlassEntity SET name = :title , capacity = :capacity , color = :color WHERE id = :id")
    fun updateCup(id: Long, title: String, capacity: String, color: String)

    @Query("DELETE FROM GlassEntity WHERE id = :id")
    fun removeCup(id: Long)


    @Query("DELETE FROM UserEntity")
    fun removeUser()

    @Query("DELETE FROM WaterEntity")
    fun removeProgress()

    @Query("DELETE FROM GlassEntity")
    fun removeCups()
}