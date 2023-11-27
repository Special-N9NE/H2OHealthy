package org.n9ne.h2ohealthy.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [ActivityEntity::class, GlassEntity::class, LeagueEntity::class, UserEntity::class, WaterEntity::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun roomDao(): RoomDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }

    }

    fun removeDatabase(appDatabase: AppDatabase) {
        appDatabase.clearAllTables()
    }
}