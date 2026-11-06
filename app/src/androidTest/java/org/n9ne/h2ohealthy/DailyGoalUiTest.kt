package org.n9ne.h2ohealthy

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.NumberPicker
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.Request
import okio.Timeout
import org.hamcrest.Matcher
import org.hamcrest.Matchers.containsString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.n9ne.common.model.GoalChange
import org.n9ne.common.model.GoalResponse
import org.n9ne.common.model.GoalState
import org.n9ne.common.model.GoalUpdate
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.source.local.UserEntity
import org.n9ne.common.source.network.ApiService
import org.n9ne.common.source.network.Client
import org.n9ne.common.source.objects.GetCups
import org.n9ne.common.source.objects.GetProgress
import org.n9ne.common.util.RepoCallback
import org.n9ne.common.util.Saver
import org.n9ne.common.util.Utils
import org.n9ne.h2ohealthy.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Proxy
import java.text.NumberFormat
import java.time.LocalDate
import com.example.home.R as HomeR
import org.n9ne.common.R as CommonR

/** All network calls are intercepted. Restores the device's original cached data. */
@RunWith(AndroidJUnit4::class)
class DailyGoalUiTest {
    @Test
    fun editScheduleRetryAndHistoryWorkWithoutChangingProduction() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        Saver.setup(context)
        val db = AppDatabase.getDatabase(context)
        val dao = db.roomDao()
        val originalUsers = dao.getUser()
        val originalWater = dao.getProgress()
        val originalToken = Saver.getToken()?.removePrefix("Bearer ")
        Client.getInstance()
        val originalApi = Client.apiService
        val today = LocalDate.now()
        val fixtureId = 2147001L
        assertTrue(originalUsers.none { it.id == fixtureId })
        val fixture = GoalFixture(fixtureId, today)
        val cups = dao.getCups()
            .map { GetCups.Cup(it.capacity, it.color, it.id.toInt(), fixtureId.toInt(), it.name) }
        var scenario: ActivityScenario<MainActivity>? = null
        fun litres(value: Int) = context.getString(
            CommonR.string.goal_litres,
            NumberFormat.getIntegerInstance(Utils.getLocal()).format(value)
        )

        fun number(value: Int) = NumberFormat.getIntegerInstance(Utils.getLocal()).format(value)
        fun waitFor(assertion: () -> Unit) {
            val deadline = System.currentTimeMillis() + 12_000
            var failure: Throwable? = null
            while (System.currentTimeMillis() < deadline) {
                try {
                    assertion(); return
                } catch (e: Throwable) {
                    failure = e; Thread.sleep(150)
                }
            }
            throw failure ?: AssertionError("UI did not settle")
        }
        try {
            db.runInTransaction {
                dao.removeUser()
                dao.insertUser(
                    UserEntity(
                        fixtureId,
                        1,
                        0,
                        "goals@example.invalid",
                        "",
                        today.minusDays(35).toString().replace('-', '/'),
                        "Goal UI Test",
                        "2000/01/01",
                        "70",
                        "175",
                        0,
                        "0",
                        "3",
                        "avatar01"
                    )
                )
            }
            Saver.saveToken("isolated-goal-ui-test")
            Client.apiService = Proxy.newProxyInstance(
                ApiService::class.java.classLoader,
                arrayOf(ApiService::class.java)
            ) { _, method, args ->
                when (method.name) {
                    "getGoals" -> FixtureCall { GoalResponse(true, "success", fixture.state()) }
                    "updateGoal" -> FixtureCall {
                        fixture.save(
                            Gson().fromJson(
                                args!![1] as String,
                                GoalUpdate::class.java
                            )
                        )
                    }

                    "getProgress" -> FixtureCall {
                        GetProgress(
                            listOf(
                                GetProgress.Data(
                                    99001,
                                    fixtureId.toInt(),
                                    "1.5",
                                    today.toString().replace('-', '/'),
                                    "12:00"
                                ),
                                GetProgress.Data(
                                    99002,
                                    fixtureId.toInt(),
                                    "1.5",
                                    today.minusDays(1).toString().replace('-', '/'),
                                    "12:00"
                                )
                            ), true, null
                        )
                    }

                    "getCups" -> FixtureCall { GetCups(cups, true, null) }
                    "toString" -> "IsolatedGoalApi"
                    else -> throw AssertionError("Unexpected API call: ${method.name}")
                }
            } as ApiService
            scenario = ActivityScenario.launch(Intent(context, MainActivity::class.java))
            waitFor { onView(withId(HomeR.id.tvTarget)).check(matches(withText(litres(3)))) }
            onView(withId(HomeR.id.btnEditGoal)).perform(click())
            onView(isAssignableFrom(NumberPicker::class.java)).perform(selectLitres(7))
            onView(withText(CommonR.string.goal_cancel)).perform(click())
            assertTrue(fixture.changes.isEmpty())

            onView(withId(HomeR.id.btnEditGoal)).perform(click())
            onView(isAssignableFrom(NumberPicker::class.java)).perform(selectLitres(4))
            onView(withText(CommonR.string.goal_tomorrow)).perform(click())
            onView(withText(CommonR.string.goal_save)).perform(click())
            waitFor { onView(withId(HomeR.id.btnEditGoal)).check(matches(isDisplayed())) }
            onView(withId(HomeR.id.tvTarget)).check(matches(withText(litres(3))))

            onView(withId(HomeR.id.btnEditGoal)).perform(click())
            onView(isAssignableFrom(NumberPicker::class.java)).perform(selectLitres(5))
            fixture.loseNextResponse = true
            onView(withText(CommonR.string.goal_save)).perform(click())
            waitFor { onView(withText(CommonR.string.goal_network)).check(matches(isDisplayed())) }
            onView(withText(CommonR.string.goal_save)).perform(click())
            waitFor { onView(withId(HomeR.id.tvTarget)).check(matches(withText(litres(5)))) }
            assertEquals(2, fixture.changes.size)
            assertEquals(fixture.requests.takeLast(2).first(), fixture.requests.last())
            assertEquals(4, fixture.state().scheduledOn(today.plusDays(1)))

            onView(withId(HomeR.id.btnActivityHistory)).perform(click())
            val todayDetails = context.getString(
                CommonR.string.goal_daily_row,
                litres(5),
                number(1500),
                number(30)
            )
            waitFor { onView(withText(containsString(todayDetails))).check(matches(isDisplayed())) }
            if (today.minusDays(1).month != today.month) onView(withContentDescription(CommonR.string.goal_previous_month)).perform(
                click()
            )
            onView(
                withText(
                    containsString(
                        context.getString(
                            CommonR.string.goal_daily_row,
                            litres(3),
                            number(1500),
                            number(50)
                        )
                    )
                )
            ).check(matches(isDisplayed()))
            onView(withText(CommonR.string.goal_close)).perform(click())
            onView(withId(HomeR.id.btnGoalHistory)).perform(click())
            onView(withText(containsString(context.getString(CommonR.string.goal_pending)))).check(
                matches(isDisplayed())
            )
            onView(withText(CommonR.string.goal_close)).perform(click())

            val cached = Gson().fromJson(dao.getGoals(fixtureId)!!.snapshot, GoalState::class.java)
            assertEquals(5, cached.targetOn(today))
            assertEquals(4, cached.targetOn(today.plusDays(1)))
            var offlineError: String? = null
            runBlocking {
                com.example.home.repo.HomeRepoLocalImpl(dao).saveGoal(
                    null,
                    GoalUpdate(9, today.toString(), "UTC", cached.revision, "offline-request"),
                    object : RepoCallback<GoalState> {
                        override fun onSuccessful(response: GoalState) {
                            fail("Offline save must be rejected")
                        }

                        override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                            offlineError = error
                        }
                    })
            }
            assertEquals("goals_network", offlineError)
            assertEquals(2, fixture.changes.size)
        } finally {
            scenario?.close()
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
            Client.apiService = originalApi
            // Instrumentation can terminate before SharedPreferences.apply flushes.
            val preferences =
                context.getSharedPreferences("APP", android.content.Context.MODE_PRIVATE)
            val editor = preferences.edit()
            if (originalToken == null) editor.remove("TOKEN") else editor.putString(
                "TOKEN",
                originalToken
            )
            assertTrue("Restoring the original session failed", editor.commit())
            db.runInTransaction {
                dao.removeUser()
                originalUsers.forEach(dao::insertUser)
                dao.removeProgress()
                originalWater.forEach(dao::insertWater)
                db.openHelper.writableDatabase.execSQL(
                    "DELETE FROM GoalCacheEntity WHERE userId = ?",
                    arrayOf(fixtureId)
                )
            }
        }
    }

    private fun selectLitres(value: Int) = object : ViewAction {
        override fun getConstraints(): Matcher<View> = isAssignableFrom(NumberPicker::class.java)
        override fun getDescription() = "Select daily goal $value litres"
        override fun perform(uiController: UiController, view: View) {
            (view as NumberPicker).value = value; uiController.loopMainThreadUntilIdle()
        }
    }

    private class GoalFixture(private val userId: Long, private val today: LocalDate) {
        val changes = mutableListOf<GoalChange>()
        val requests = mutableListOf<String>()

        @Volatile
        var loseNextResponse = false

        @Synchronized
        fun state() =
            GoalState(userId, today.toString(), 3, null, changes.size.toLong(), changes.toList())

        @Synchronized
        fun save(update: GoalUpdate): GoalResponse {
            assertEquals(changes.size.toLong(), update.revision)
            changes.add(
                GoalChange(
                    changes.size + 1L,
                    update.target,
                    state().targetOn(LocalDate.parse(update.effectiveDate)),
                    update.effectiveDate,
                    "$today 12:00:00",
                    update.timezone,
                )
            )

            if (loseNextResponse) {
                loseNextResponse = false; throw IOException("Simulated lost response after commit")
            }
            return GoalResponse(true, "success", state())
        }
    }

    private class FixtureCall<T>(private val value: () -> T) : Call<T> {
        private var cancelled = false
        override fun execute(): Response<T> = Response.success(value())
        override fun enqueue(callback: Callback<T>) {
            Handler(Looper.getMainLooper()).postDelayed({
                if (!cancelled) try {
                    callback.onResponse(this, execute())
                } catch (error: Throwable) {
                    callback.onFailure(this, error)
                }
            }, 100)
        }

        override fun isExecuted() = false
        override fun cancel() {
            cancelled = true
        }

        override fun isCanceled() = cancelled
        override fun clone(): Call<T> = FixtureCall(value)
        override fun request(): Request =
            Request.Builder().url("https://goals-test.invalid/").build()

        override fun timeout() = Timeout()
    }
}
