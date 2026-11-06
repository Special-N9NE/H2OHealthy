package org.n9ne.h2ohealthy

import android.content.Intent
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
import org.hamcrest.Matcher
import org.hamcrest.Matchers.containsString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.n9ne.common.BuildConfig
import org.n9ne.common.model.GoalUpdate
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.source.network.Client
import org.n9ne.common.source.objects.Auth
import org.n9ne.common.util.Mapper.toUser
import org.n9ne.common.util.Mapper.toUserEntity
import org.n9ne.common.util.Saver
import org.n9ne.common.util.Utils
import org.n9ne.h2ohealthy.ui.MainActivity
import java.text.NumberFormat
import java.time.LocalDate
import java.util.UUID
import com.example.home.R as HomeR
import org.n9ne.common.R as CommonR

/** Opt-in live integration test. Requires dev/schema.sql and seed data on loopback. */
@RunWith(AndroidJUnit4::class)
class LocalBackendGoalTest {
    @Test
    fun realPhpAndMariaDbSupportGoalEditingAndHistory() {
        assumeTrue(
            "Use -PlocalBackendUrl=http://127.0.0.1:8088/",
            BuildConfig.API_BASE_URL == "http://127.0.0.1:8088/"
        )
        val arguments = InstrumentationRegistry.getArguments()
        val email = arguments.getString("localEmail")
        val password = arguments.getString("localPassword")
        assumeTrue(
            "Pass the local fixture credentials",
            email == "local-goals@example.invalid" && !password.isNullOrEmpty()
        )
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        Saver.setup(context)
        val api = Client.getInstance().getApiService()
        val gson = Gson()
        val login = api.login(gson.toJson(Auth.Login(email!!, Utils.encrypt(password!!)))).execute()
        assertTrue("Local PHP login failed", login.isSuccessful && login.body()?.status == true)
        val result = login.body()!!
        val user = result.user!!.first().toUser()
        val token = "Bearer ${result.message}"
        assertEquals("This test must only use the seeded local account", 1L, user.id)
        val db = AppDatabase.getDatabase(context)
        db.runInTransaction {
            db.roomDao().removeUser()
            db.roomDao().insertUser(user.toUserEntity())
        }
        assertTrue(
            context.getSharedPreferences("APP", android.content.Context.MODE_PRIVATE).edit()
                .putString("TOKEN", result.message).putString("EMAIL", email).commit()
        )
        val payload = gson.toJson(mapOf("timezone" to java.time.ZoneId.systemDefault().id))
        val initial = api.getGoals(token, payload).execute().body()!!.data!!
        assertEquals("Reset the local fixture goal history before rerunning", 0L, initial.revision)

        fun litres(n: Int) = context.getString(
            CommonR.string.goal_litres,
            NumberFormat.getIntegerInstance(Utils.getLocal()).format(n)
        )

        fun number(n: Int) = NumberFormat.getIntegerInstance(Utils.getLocal()).format(n)
        fun waitFor(assertion: () -> Unit) {
            val deadline = System.currentTimeMillis() + 15_000
            var failure: Throwable? = null
            while (System.currentTimeMillis() < deadline) {
                try {
                    assertion(); return
                } catch (e: Throwable) {
                    failure = e; Thread.sleep(150)
                }
            }
            throw failure ?: AssertionError("Local goal UI did not settle")
        }
        ActivityScenario.launch<MainActivity>(Intent(context, MainActivity::class.java)).use {
            waitFor { onView(withId(HomeR.id.tvTarget)).check(matches(withText(litres(3)))) }
            onView(withId(HomeR.id.btnEditGoal)).perform(click())
            onView(isAssignableFrom(NumberPicker::class.java)).perform(selectLitres(4))
            onView(withText(CommonR.string.goal_tomorrow)).perform(click())
            onView(withText(CommonR.string.goal_save)).perform(click())
            waitFor { onView(withId(HomeR.id.btnEditGoal)).check(matches(isDisplayed())) }
            onView(withId(HomeR.id.tvTarget)).check(matches(withText(litres(3))))

            onView(withId(HomeR.id.btnEditGoal)).perform(click())
            onView(isAssignableFrom(NumberPicker::class.java)).perform(selectLitres(5))
            onView(withText(CommonR.string.goal_save)).perform(click())
            waitFor { onView(withId(HomeR.id.tvTarget)).check(matches(withText(litres(5)))) }
            onView(withId(HomeR.id.btnActivityHistory)).perform(click())
            val todayRow = context.getString(
                CommonR.string.goal_daily_row,
                litres(5),
                number(1500),
                number(30)
            )
            waitFor { onView(withText(containsString(todayRow))).check(matches(isDisplayed())) }
            if (LocalDate.now().minusDays(1).month != LocalDate.now().month) onView(
                withContentDescription(CommonR.string.goal_previous_month)
            ).perform(click())
            val yesterdayRow = context.getString(
                CommonR.string.goal_daily_row,
                litres(3),
                number(1500),
                number(50)
            )
            onView(withText(containsString(yesterdayRow))).check(matches(isDisplayed()))
            onView(withText(CommonR.string.goal_close)).perform(click())
            onView(withId(HomeR.id.btnGoalHistory)).perform(click())
            onView(withText(containsString(context.getString(CommonR.string.goal_pending)))).check(
                matches(isDisplayed())
            )
            onView(withText(CommonR.string.goal_close)).perform(click())

            val state = api.getGoals(token, payload).execute().body()!!.data!!
            assertEquals(2, state.changes.size)
            assertEquals(5, state.currentTarget)
            assertEquals(4, state.tomorrowTarget)
            val saved = state.changes.last()
            val replay = api.updateGoal(
                token,
                gson.toJson(GoalUpdate(5, saved.effectiveDate, saved.timezone, 1))
            ).execute()
            assertTrue(replay.isSuccessful)
            assertEquals(2, replay.body()!!.data!!.changes.size)
            val stale = api.updateGoal(
                token,
                gson.toJson(
                    GoalUpdate(
                        6,
                        LocalDate.now().toString(),
                        java.time.ZoneId.systemDefault().id,
                        0,
                        UUID.randomUUID().toString()
                    )
                )
            ).execute()
            assertEquals(409, stale.code())
            assertEquals(
                401,
                api.getGoals("Bearer not-a-valid-local-token", payload).execute().code()
            )
        }
    }

    private fun selectLitres(value: Int) = object : ViewAction {
        override fun getConstraints(): Matcher<View> = isAssignableFrom(NumberPicker::class.java)
        override fun getDescription() = "Select local-test daily goal $value litres"
        override fun perform(uiController: UiController, view: View) {
            (view as NumberPicker).value = value; uiController.loopMainThreadUntilIdle()
        }
    }
}
