package org.n9ne.h2ohealthy.data.repo

import com.google.gson.Gson
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.source.network.Client
import org.n9ne.h2ohealthy.data.source.objects.GetCups
import org.n9ne.h2ohealthy.data.source.objects.InsertActivity
import org.n9ne.h2ohealthy.data.source.objects.Message
import org.n9ne.h2ohealthy.util.Mapper.toCups
import org.n9ne.h2ohealthy.util.Messages
import org.n9ne.h2ohealthy.util.RepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepoApiImpl(private val client: Client) : MainRepo {

    override suspend fun insertWater(water: Activity,token: String? ,callback: RepoCallback<Long>) {
        val json = Gson().toJson(InsertActivity(water.date, water.amount, water.time))
        client.getApiService().addActivity(json, token!!)
            .enqueue(object : Callback<Message> {
                override fun onResponse(
                    call: Call<Message>, response: Response<Message>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!

                        if (result.status) {
                            callback.onSuccessful(result.message.toLong())
                        } else {
                            val message = result.message
                            if (message == "-1")
                                callback.onError(message, isToken = true)
                            else
                                callback.onError(message)
                        }

                    } else {
                        val result = response.code().toString()
                        callback.onError(result)
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {

                    if (t.message?.contains(Messages.NO_INTERNET) == true) {
                        val result = Messages.errorNetwork
                        callback.onError(result, true)
                    } else {
                        val result = t.message.toString()
                        callback.onError(result)
                    }
                }
            })
    }

    override suspend fun getCups(token: String?, callback: RepoCallback<List<Cup>>) {
        client.getApiService().getCups(token!!)
            .enqueue(object : Callback<GetCups> {
                override fun onResponse(
                    call: Call<GetCups>, response: Response<GetCups>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!

                        if (result.status) {
                            callback.onSuccessful(result.toCups())
                        } else {
                            val message = result.message!!
                            if (message == "-1")
                                callback.onError(message, isToken = true)
                            else
                                callback.onError(message)
                        }

                    } else {
                        val result = response.code().toString()
                        callback.onError(result)
                    }
                }

                override fun onFailure(call: Call<GetCups>, t: Throwable) {

                    if (t.message?.contains(Messages.NO_INTERNET) == true) {
                        val result = Messages.errorNetwork
                        callback.onError(result, true)
                    } else {
                        val result = t.message.toString()
                        callback.onError(result)
                    }
                }
            })
    }
}