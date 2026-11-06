package com.example.home.repo

import org.n9ne.common.model.Activity
import org.n9ne.common.model.GoalHistoryPage
import org.n9ne.common.model.GoalState
import org.n9ne.common.model.GoalUpdate
import org.n9ne.common.util.RepoCallback

interface HomeRepo {
    suspend fun getGoalHistory(
        token: String?,
        before: Long?,
        callback: RepoCallback<GoalHistoryPage>
    )

    suspend fun getActivityHistory(
        token: String?,
        start: java.time.LocalDate,
        end: java.time.LocalDate,
        callback: RepoCallback<List<Activity>>
    )

    suspend fun getGoals(token: String?, callback: RepoCallback<GoalState>)
    suspend fun saveGoal(
        token: String?,
        update: GoalUpdate,
        callback: RepoCallback<GoalState>
    )

    suspend fun getTarget(token: String?, callback: RepoCallback<Int>)
    suspend fun getProgress(
        token: String?,
        callback: RepoCallback<List<Activity>>
    )

    suspend fun updateWater(
        id: Long,
        amount: String,
        token: String?,
        callback: RepoCallback<String>
    ) {
    }

    suspend fun removeWater(id: Long, token: String?, callback: RepoCallback<String>) {}
}
