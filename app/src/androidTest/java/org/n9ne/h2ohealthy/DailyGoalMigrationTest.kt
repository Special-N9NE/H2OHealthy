package org.n9ne.h2ohealthy

import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.n9ne.common.model.GoalChange
import org.n9ne.common.model.GoalState
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.source.local.GoalCacheEntity
import java.time.LocalDate
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class DailyGoalMigrationTest {
    @Test
    fun upgradePreservesVersionNineDataAndCachesIndependentAccountTimelines() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val name = "goal-migration-${UUID.randomUUID()}"
        val path = context.getDatabasePath(name)
        path.parentFile!!.mkdirs()
        SQLiteDatabase.openOrCreateDatabase(path, null).use { old ->
            old.execSQL("CREATE TABLE ActivityEntity (id INTEGER NOT NULL PRIMARY KEY, type TEXT NOT NULL)")
            old.execSQL("CREATE TABLE GlassEntity (id INTEGER NOT NULL PRIMARY KEY, idUser INTEGER, name TEXT NOT NULL, capacity TEXT NOT NULL, color TEXT NOT NULL)")
            old.execSQL("CREATE TABLE LeagueEntity (id INTEGER NOT NULL PRIMARY KEY, idUser INTEGER, name TEXT NOT NULL, code TEXT NOT NULL)")
            old.execSQL("CREATE TABLE WaterEntity (id INTEGER NOT NULL PRIMARY KEY, idUser INTEGER, date TEXT NOT NULL, amount TEXT NOT NULL, time TEXT NOT NULL)")
            old.execSQL("CREATE TABLE UserEntity (id INTEGER NOT NULL PRIMARY KEY, idActivity INTEGER NOT NULL, idLeague INTEGER NOT NULL, email TEXT NOT NULL, password TEXT NOT NULL, date TEXT NOT NULL, name TEXT NOT NULL, birthdate TEXT NOT NULL, weight TEXT NOT NULL, height TEXT NOT NULL, gender INTEGER NOT NULL, score TEXT NOT NULL, target TEXT NOT NULL, profile TEXT NOT NULL)")
            old.execSQL("INSERT INTO UserEntity VALUES (42, 1, 0, 'test@example.invalid', '', '2026/10/01', 'Migration Test', '2000/01/01', '70', '175', 0, '10', '3', 'avatar01')")
            old.execSQL("INSERT INTO WaterEntity VALUES (17, 42, '2026/12/31', '1.5', '12:00')")
            old.execSQL("INSERT INTO GlassEntity VALUES (8, 42, 'Test Cup', '250', 'blue')")
            old.version = 9
        }
        val db = Room.databaseBuilder(context, AppDatabase::class.java, name)
            .addMigrations(AppDatabase.MIGRATION_9_10).build()
        try {
            val dao = db.roomDao()
            assertEquals(10, db.openHelper.writableDatabase.version)
            assertEquals("3", dao.getUser().single().target)
            assertEquals("10", dao.getUser().single().score)
            assertEquals("1.5", dao.getProgress().single().amount)
            assertEquals("Test Cup", dao.getCups().single().name)
            val date = LocalDate.of(2026, 12, 31)
            val state = GoalState(
                42, date.toString(), 3, 5, 7, listOf(
                    GoalChange(
                        7,
                        5,
                        3,
                        date.plusDays(1).toString(),
                        "2026-12-31 12:00:00",
                        "Asia/Tehran",
                        "migration-test"
                    )
                )
            )
            dao.cacheGoals(GoalCacheEntity(42, Gson().toJson(state)))
            val offline = Gson().fromJson(dao.getGoals(42)!!.snapshot, GoalState::class.java)
            assertEquals(3, offline.targetOn(date))
            assertEquals(5, offline.targetOn(date.plusDays(1)))
            assertNull(dao.getGoals(43))
            dao.updateUserTarget(43, "7")
            assertEquals("3", dao.getUser().single().target)
        } finally {
            db.close()
            context.deleteDatabase(name)
        }
    }
}
