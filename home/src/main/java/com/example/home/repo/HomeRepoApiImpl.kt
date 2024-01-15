package com.example.home.repo

import com.google.gson.Gson
import org.n9ne.common.BaseRepoImpl
import org.n9ne.common.model.Activity
import org.n9ne.common.source.network.Client
import org.n9ne.common.source.objects.UpdateActivity
import org.n9ne.common.util.Mapper.toActivities
import org.n9ne.common.util.RepoCallback

class HomeRepoApiImpl (private val client: Client) :
    BaseRepoImpl(), HomeRepo {

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