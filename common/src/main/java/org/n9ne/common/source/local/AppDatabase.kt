package org.n9ne.common.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = [ActivityEntity::class, GlassEntity::class, LeagueEntity::class, UserEntity::class, WaterEntity::class, GoalCacheEntity::class],
    version = 10
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun roomDao(): RoomDao


    companion object {
        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `GoalCacheEntity` (`userId` INTEGER NOT NULL, `snapshot` TEXT NOT NULL, PRIMARY KEY(`userId`))")
            }
        }

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
                ).addMigrations(MIGRATION_9_10).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }

    }

    fun removeDatabase() {
        this.clearAllTables()
    }
}
