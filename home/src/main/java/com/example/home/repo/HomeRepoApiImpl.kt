package com.example.home.repo

import com.google.gson.Gson
import com.google.gson.JsonParseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import org.n9ne.common.BaseRepoImpl
import org.n9ne.common.model.Activity
import org.n9ne.common.model.GoalHistoryPage
import org.n9ne.common.model.GoalResponse
import org.n9ne.common.model.GoalState
import org.n9ne.common.model.GoalUpdate
import org.n9ne.common.source.network.Client
import org.n9ne.common.source.objects.UpdateActivity
import org.n9ne.common.util.Mapper.toActivities
import org.n9ne.common.util.RepoCallback
import retrofit2.Call
import java.time.LocalDate
import java.time.ZoneId

class HomeRepoApiImpl(private val client: Client) :
    BaseRepoImpl(), HomeRepo {
    override suspend fun getGoalHistory(
        token: String?,
        before: Long?,
        callback: RepoCallback<GoalHistoryPage>
    ) {
        try {
            val response = client.getApiService()
                .getGoalHistory(token!!, Gson().toJson(mapOf("before_id" to before))).execute()
            val page = response.body()?.data
            if (response.isSuccessful && response.body()?.status == true && page != null) callback.onSuccessful(
                page
            )
            else callback.onError("history_unavailable")
        } catch (e: Exception) {
            callback.onError("history_unavailable", true)
            e.printStackTrace()
        }
    }

    override suspend fun getActivityHistory(
        token: String?,
        start: LocalDate,
        end: LocalDate,
        callback: RepoCallback<List<Activity>>
    ) {
        val call = client.getApiService().getActivityHistory(
            token!!,
            Gson().toJson(mapOf("start" to start.toString(), "end" to end.toString()))
        )
        getResponse(call, callback).collect {
            if (it.status && it.data != null) callback.onSuccessful(it.data!!.toActivities())
            else callback.onError(it.message ?: "history_unavailable")
        }
    }

    override suspend fun getGoals(
        token: String?,
        callback: RepoCallback<GoalState>
    ) {
        val payload = Gson().toJson(mapOf("timezone" to ZoneId.systemDefault().id))
        goalCall(client.getApiService().getGoals(token!!, payload), callback)
    }

    override suspend fun saveGoal(
        token: String?,
        update: GoalUpdate,
        callback: RepoCallback<GoalState>
    ) {
        goalCall(client.getApiService().updateGoal(token!!, Gson().toJson(update)), callback)
    }

    private suspend fun goalCall(
        call: Call<GoalResponse>,
        callback: RepoCallback<GoalState>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response = call.execute()
                val body = response.body()
                val state = body?.data
                if (response.isSuccessful && body?.status == true && state != null) {
                    callback.onSuccessful(state)
                } else {
                    callback.onError(
                        when (response.code()) {
                            409 -> "goal_conflict"
                            400 -> "invalid_goal"
                            401 -> "-1"
                            else -> "goals_unavailable"
                        }
                    )
                }
            } catch (e: IOException) {
                callback.onError("goals_network", true)
                e.printStackTrace()
            } catch (e: JsonParseException) {
                callback.onError("goals_unavailable")
                e.printStackTrace()
            }
        }
    }


    override suspend fun getTarget(token: String?, callback: RepoCallback<Int>) {
        val call = client.getApiService().getTarget(token!!)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.message.toInt())
            } else {
                handleError(it.message, callback)
            }
        }
    }

    override suspend fun getProgress(token: String?, callback: RepoCallback<List<Activity>>) {
        val call = client.getApiService().getProgress(token!!)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.data!!.toActivities())
            } else {
                handleError(it.message!!, callback)
            }
        }
    }


    override suspend fun updateWater(
        id: Long,
        amount: String,
        token: String?,
        callback: RepoCallback<String>
    ) {
        val json = Gson().toJson(UpdateActivity(id.toString(), amount))
        val call = client.getApiService().updateActivity(token!!, json)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.message)
            } else {
                handleError(it.message, callback)
            }
        }
    }

    override suspend fun removeWater(id: Long, token: String?, callback: RepoCallback<String>) {
        val call = client.getApiService().removeActivity(token!!, id.toString())
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.message)
            } else {
                handleError(it.message, callback)
            }
        }
    }
}
