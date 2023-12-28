package org.n9ne.h2ohealthy.data

import com.google.gson.Gson
import org.n9ne.common.BaseRepoImpl
import org.n9ne.common.model.Cup
import org.n9ne.common.source.network.Client
import org.n9ne.common.source.objects.InsertActivity
import org.n9ne.common.util.Mapper.toCups
import org.n9ne.common.util.RepoCallback

class MainRepoApiImpl(private val client: Client) : BaseRepoImpl(), MainRepo {
    override suspend fun insertWater(
        water: org.n9ne.common.model.Activity,
        token: String?,
        callback: RepoCallback<Long>
    ) {
        val json = Gson().toJson(InsertActivity(water.date, water.amount, water.time))
        val call = client.getApiService().addActivity(json, token!!)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.message.toLong())
            } else {
                handleError(it.message, callback)
            }
        }
    }

    override suspend fun getCups(token: String?, callback: RepoCallback<List<Cup>>) {

        val call = client.getApiService().getCups(token!!)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.toCups())
            } else {
                handleError(it.message!!, callback)
            }
        }
    }
}