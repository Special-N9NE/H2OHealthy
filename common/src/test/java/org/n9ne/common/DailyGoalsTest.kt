package org.n9ne.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.n9ne.common.model.GoalChange
import org.n9ne.common.model.GoalMath
import org.n9ne.common.model.GoalState
import java.time.LocalDate

class DailyGoalsTest {
    private val today = LocalDate.of(2026, 12, 31)
    private fun change(id: Long, litres: Int, date: LocalDate) = GoalChange(
        id,
        litres,
        3,
        date.toString(),
        "Asia/Tehran",
    )

    private fun state(vararg changes: GoalChange) = GoalState(
        1,
        today.toString(),
        3,
        null,
        changes.maxOfOrNull { it.id } ?: 0,
        changes.toList())

    @Test
    fun oldDaysUseThreeLitresAndRetainTheirPercentages() {
        val goals = state(change(1, 5, today))
        assertEquals(3, goals.targetOn(today.minusDays(1)))
        assertEquals(50, GoalMath.percentage(1500.0, goals.targetOn(today.minusDays(1))))
        assertEquals(30, GoalMath.percentage(1500.0, goals.targetOn(today)))
    }

    @Test
    fun scheduledGoalActivatesOfflineAcrossYearBoundary() {
        val cached = state(change(1, 5, today), change(2, 4, today.plusDays(1)))
        assertEquals(5, cached.targetOn(today))
        assertEquals(4, cached.scheduledOn(today.plusDays(1)))
        assertEquals(4, cached.targetOn(today.plusDays(1)))
        assertNull(cached.scheduledOn(today.plusDays(2)))
    }

    @Test
    fun laterTodayEditDoesNotOverrideTomorrow() {
        val goals = state(change(1, 5, today), change(2, 4, today.plusDays(1)), change(3, 6, today))
        assertEquals(6, goals.targetOn(today))
        assertEquals(4, goals.targetOn(today.plusDays(1)))
        assertEquals(3, goals.changes.size)
    }

    @Test
    fun sameDateUsesLatestRevisionRegardlessOfListOrder() {
        assertEquals(8, state(change(9, 8, today), change(2, 4, today)).targetOn(today))
    }

    @Test
    fun unchangedDatesCarryForwardWithoutDailySnapshots() {
        assertEquals(5, state(change(1, 5, today)).targetOn(today.plusMonths(3)))
    }

    @Test
    fun legacyNonDefaultGoalStartsTodayOnly() {
        val goals = GoalState.legacy(2, 4, today)
        assertEquals(4, goals.targetOn(today))
        assertEquals(3, goals.targetOn(today.minusDays(1)))
        assertEquals(2L, goals.userId)
    }

    @Test
    fun invalidAmountsCannotDivideByZeroAndOverachievementIsRetained() {
        assertEquals(0, GoalMath.percentage(0.0, 3))
        assertEquals(0, GoalMath.percentage(-1.0, 3))
        assertEquals(150, GoalMath.percentage(4500.0, 3))
    }

    @Test
    fun waterDatesSupportExistingSlashFormatAndRejectInvalidDates() {
        assertEquals(today, GoalMath.date("2026/12/31"))
        assertEquals(LocalDate.of(2027, 1, 2), GoalMath.date("2027/1/2"))
        assertNull(GoalMath.date("2026/02/30"))
    }
}
