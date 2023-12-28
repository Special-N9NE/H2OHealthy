package org.n9ne.profile.repo

import com.google.gson.Gson
import org.n9ne.common.model.Activity
import org.n9ne.common.model.CreateLeague
import org.n9ne.common.model.Cup
import org.n9ne.common.model.GetLeague
import org.n9ne.common.model.UpdateUser
import org.n9ne.common.model.User
import org.n9ne.common.source.network.Client
import org.n9ne.common.source.objects.GetCups
import org.n9ne.common.source.objects.GetMembers
import org.n9ne.common.source.objects.GetProgress
import org.n9ne.common.source.objects.GetUser
import org.n9ne.common.source.objects.InsertCup
import org.n9ne.common.source.objects.Message
import org.n9ne.common.source.objects.RenameLeague
import org.n9ne.common.source.objects.UpdateCup
import org.n9ne.common.util.Mapper.toActivities
import org.n9ne.common.util.Mapper.toCups
import org.n9ne.common.util.Mapper.toMembers
import org.n9ne.common.util.Mapper.toUser
import org.n9ne.common.util.Messages
import org.n9ne.common.util.RepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRepoApiImpl(private val client: Client) : ProfileRepo {
    override suspend fun getAllActivity(token: String?, callback: RepoCallback<List<Activity>>) {
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

    override suspend fun getUser(token: String?, callback: RepoCallback<User>) {
        client.getApiService().getUser(token!!)
            .enqueue(object : Callback<GetUser> {
                override fun onResponse(
                    call: Call<GetUser>, response: Response<GetUser>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!

                        if (result.status) {
                            callback.onSuccessful(result.user!!.toUser())
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

                override fun onFailure(call: Call<GetUser>, t: Throwable) {

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

    override suspend fun addCup(cup: Cup, token: String?, callback: RepoCallback<Long>) {
        val json = Gson().toJson(InsertCup(cup.title, cup.capacity.toString(), cup.color))
        client.getApiService().addCup(token!!, json)
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

    override suspend fun updateCup(cup: Cup, callback: RepoCallback<String>) {
        val json = Gson().toJson(
            UpdateCup(
                cup.id.toString(),
                cup.title,
                cup.capacity.toString(),
                cup.color
            )
        )
        client.getApiService().updateCup(json)
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

    override suspend fun removeCup(cup: Cup, callback: RepoCallback<String>) {

        client.getApiService().removeCup(cup.id!!.toString())
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

    override suspend fun updateUser(
        date: UpdateUser,
        token: String?,
        callback: RepoCallback<User>
    ) {
        val json = Gson().toJson(date)
        client.getApiService().updateUser(token!!, json)
            .enqueue(object : Callback<GetUser> {
                override fun onResponse(
                    call: Call<GetUser>, response: Response<GetUser>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!

                        if (result.status) {
                            callback.onSuccessful(result.user!!.toUser())
                        } else {
                            val message = result.message
                            if (message == "-1")
                                callback.onError(message, isToken = true)
                            else
                                callback.onError(message!!)
                        }

                    } else {
                        val result = response.code().toString()
                        callback.onError(result)
                    }
                }

                override fun onFailure(call: Call<GetUser>, t: Throwable) {

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

    override suspend fun joinLeague(code: String, token: String?, callback: RepoCallback<Long>) {
        client.getApiService().joinLeague(token!!, code)
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

    override suspend fun createLeague(
        name: String,
        token: String?,
        callback: RepoCallback<CreateLeague>
    ) {
        client.getApiService().createLeague(token!!, name)
            .enqueue(object : Callback<Message> {
                override fun onResponse(
                    call: Call<Message>, response: Response<Message>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!

                        if (result.status) {
                            callback.onSuccessful(
                                org.n9ne.common.model.CreateLeague(
                                    result.id!!,
                                    result.message
                                )
                            )
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

    override suspend fun getLeagueUsers(token: String?, callback: RepoCallback<GetLeague>) {
        client.getApiService().getLeague(token!!)
            .enqueue(object : Callback<GetMembers> {
                override fun onResponse(
                    call: Call<GetMembers>, response: Response<GetMembers>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!

                        if (result.status) {
                            val members = result.toMembers()
                            val name = result.name!!
                            val code = result.code!!
                            val adminEmail = result.admin!!

                            callback.onSuccessful(GetLeague(name, code, adminEmail, members))
                        } else {
                            val message = result.message
                            if (message == "-1")
                                callback.onError(message, isToken = true)
                            else
                                callback.onError(message!!)
                        }

                    } else {
                        val result = response.code().toString()
                        callback.onError(result)
                    }
                }

                override fun onFailure(call: Call<GetMembers>, t: Throwable) {

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

    override suspend fun renameLeague(name: String, code: String, callback: RepoCallback<String>) {
        val json = Gson().toJson(RenameLeague(name, code))
        client.getApiService().renameLeague(json)
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

    override suspend fun leaveLeague(token: String?, callback: RepoCallback<String>) {
        client.getApiService().leaveLeague(token!!)
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