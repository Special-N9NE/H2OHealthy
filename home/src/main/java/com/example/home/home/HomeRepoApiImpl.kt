package com.example.home.home

import com.google.gson.Gson
import org.n9ne.common.model.Activity
import org.n9ne.common.source.network.Client
import org.n9ne.common.source.objects.GetProgress
import org.n9ne.common.source.objects.Message
import org.n9ne.common.source.objects.UpdateActivity
import org.n9ne.common.util.Mapper.toActivities
import org.n9ne.common.util.Messages
import org.n9ne.common.util.RepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepoApiImpl(private val client: Client) : HomeRepo {

    override suspend fun getTarget(token: String?, callback: RepoCallback<Int>) {
        client.getApiService().getTarget(token!!)
            .enqueue(object : Callback<Message> {
                override fun onResponse(
                    call: Call<Message>, response: Response<Message>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!

                        if (result.status) {
                            callback.onSuccessful(result.message.toInt())
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

    override suspend fun getProgress(token: String?, callback: RepoCallback<List<Activity>>) {
        client.getApiService().getProgress(token!!)
            .enqueue(object : Callback<GetProgress> {
                override fun onResponse(
                    call: Call<GetProgress>, response: Response<GetProgress>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!

                        if (result.status) {
                            callback.onSuccessful(result.data!!.toActivities())
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

                override fun onFailure(call: Call<GetProgress>, t: Throwable) {

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


    override suspend fun updateWater(
        id: Long,
        amount: String,
        token: String?,
        callback: RepoCallback<String>
    ) {
        val json = Gson().toJson(UpdateActivity(id.toString(), amount))
        client.getApiService().updateActivity(token!!, json)
            .enqueue(object : Callback<Message> {
                override fun onResponse(
                    call: Call<Message>, response: Response<Message>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!

                        if (result.status) {
                            callback.onSuccessful(result.message)
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

    override suspend fun removeWater(id: Long,token: String?, callback: RepoCallback<String>) {
        client.getApiService().removeActivity(token!! , id.toString())
            .enqueue(object : Callback<Message> {
                override fun onResponse(
                    call: Call<Message>, response: Response<Message>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!

                        if (result.status) {
                            callback.onSuccessful(result.message)
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

}