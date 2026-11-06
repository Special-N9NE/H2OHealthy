package com.example.home.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.n9ne.common.model.Activity
import org.n9ne.common.model.GoalHistoryPage
import org.n9ne.common.model.GoalMath
import org.n9ne.common.model.GoalState
import org.n9ne.common.model.GoalUpdate
import org.n9ne.common.source.local.RoomDao
import org.n9ne.common.util.Mapper.toActivityList
import org.n9ne.common.util.RepoCallback

class HomeRepoLocalImpl(private val dao: RoomDao) : HomeRepo {
    override suspend fun getGoalHistory(
        token: String?,
        before: Long?,
        callback: RepoCallback<GoalHistoryPage>
    ) {
        getGoals(token, object : RepoCallback<GoalState> {
            override fun onSuccessful(response: GoalState) {
                val rows = response.changes.filter { before == null || it.id < before }
                    .sortedByDescending { it.id }
                val page = rows.take(30)
                callback.onSuccessful(
                    GoalHistoryPage(
                        page,
                        if (rows.size > 30) page.last().id else null
                    )
                )
            }

            override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                callback.onError(error, isNetwork, isToken)
            }
        })
    }

    override suspend fun getActivityHistory(
        token: String?,
        start: java.time.LocalDate,
        end: java.time.LocalDate,
        callback: RepoCallback<List<org.n9ne.common.model.Activity>>
    ) {
        withContext(Dispatchers.IO) {
            callback.onSuccessful(dao.getProgress().toActivityList().filter {
                GoalMath.date(it.date)?.let { date -> date in start..<end } == true
            })
        }
    }

    override suspend fun getGoals(
        token: String?,
        callback: RepoCallback<GoalState>
    ) {
        withContext(Dispatchers.IO) {
            val user = dao.getUser().firstOrNull()
            if (user == null) {
                callback.onError("no user")
                return@withContext
            }
            val cached = dao.getGoals(user.id)?.let {
                runCatching {
                    com.google.gson.Gson()
                        .fromJson(it.snapshot, GoalState::class.java)
                }.getOrNull()
            }
            callback.onSuccessful(
                cached ?: GoalState.legacy(
                    user.id,
                    user.target.toIntOrNull() ?: 3
                )
            )
        }
    }

    override suspend fun saveGoal(
        token: String?,
        update: GoalUpdate,
        callback: RepoCallback<GoalState>
    ) {
        callback.onError("goals_network", true)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getTarget(token: String?, callback: RepoCallback<Int>) {
        withContext(Dispatchers.IO) {
            val users = async { dao.getUser() }
            users.invokeOnCompletion {
                if (users.getCompleted().isEmpty()) {
                    callback.onError("no user")
                } else
                    callback.onSuccessful(users.getCompleted()[0].target.toInt())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getProgress(
        token: String?,
        callback: RepoCallback<List<Activity>>
    ) {
        withContext(Dispatchers.IO) {
            val progress = async { dao.getProgress() }
            progress.invokeOnCompletion {
                val result = progress.getCompleted().toActivityList()

                callback.onSuccessful(result)
            }
        }
    }

    override suspend fun updateWater(
        id: Long,
        amount: String,
        token: String?,
        callback: RepoCallback<String>
    ) {
        dao.updateWater(id, amount)
        callback.onSuccessful("Success")
    }

    override suspend fun removeWater(id: Long, token: String?, callback: RepoCallback<String>) {
        dao.removeWater(id)
        callback.onSuccessful("Success")
    }
}
