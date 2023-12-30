package org.n9ne.profile.repo

import com.google.gson.Gson
import org.n9ne.common.BaseRepoImpl
import org.n9ne.common.model.Activity
import org.n9ne.common.model.CreateLeague
import org.n9ne.common.model.Cup
import org.n9ne.common.model.GetLeague
import org.n9ne.common.model.UpdateUser
import org.n9ne.common.model.User
import org.n9ne.common.source.network.Client
import org.n9ne.common.source.objects.InsertCup
import org.n9ne.common.source.objects.RenameLeague
import org.n9ne.common.source.objects.UpdateCup
import org.n9ne.common.util.Mapper.toActivities
import org.n9ne.common.util.Mapper.toCups
import org.n9ne.common.util.Mapper.toMembers
import org.n9ne.common.util.Mapper.toUser
import org.n9ne.common.util.RepoCallback

class ProfileRepoApiImpl(private val client: Client) : BaseRepoImpl(), ProfileRepo {
    override suspend fun getAllActivity(token: String?, callback: RepoCallback<List<Activity>>) {
        val call = client.getApiService().getProgress(token!!)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.data!!.toActivities())
            } else {
                handleError(it.message!!, callback)
            }
        }
    }

    override suspend fun getUser(token: String?, callback: RepoCallback<User>) {
        val call = client.getApiService().getUser(token!!)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.user!!.toUser())
            } else {
                handleError(it.message!!, callback)
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

    override suspend fun addCup(cup: Cup, token: String?, callback: RepoCallback<Long>) {
        val json = Gson().toJson(InsertCup(cup.title, cup.capacity.toString(), cup.color))
        val call = client.getApiService().addCup(token!!, json)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.message.toLong())
            } else {
                handleError(it.message, callback)
            }
        }
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
        val call = client.getApiService().updateCup(json)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.message)
            } else {
                handleError(it.message, callback)
            }
        }
    }

    override suspend fun removeCup(cup: Cup, callback: RepoCallback<String>) {
        val call = client.getApiService().removeCup(cup.id!!.toString())
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.message)
            } else {
                handleError(it.message, callback)
            }
        }
    }

    override suspend fun updateUser(
        date: UpdateUser,
        token: String?,
        callback: RepoCallback<User>
    ) {

        val json = Gson().toJson(date)
        val call = client.getApiService().updateUser(token!!, json)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.user!!.toUser())
            } else {
                handleError(it.message!!, callback)
            }
        }
    }

    override suspend fun joinLeague(code: String, token: String?, callback: RepoCallback<Long>) {
        val call = client.getApiService().joinLeague(token!!, code)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.message.toLong())
            } else {
                handleError(it.message, callback)
            }
        }
    }

    override suspend fun createLeague(
        name: String,
        token: String?,
        callback: RepoCallback<CreateLeague>
    ) {
        val call = client.getApiService().createLeague(token!!, name)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(
                    CreateLeague(
                        it.id!!,
                        it.message
                    )
                )
            } else {
                handleError(it.message, callback)
            }
        }
    }

    override suspend fun getLeagueUsers(token: String?, callback: RepoCallback<GetLeague>) {
        val call = client.getApiService().getLeague(token!!)
        getResponse(call, callback).collect {
            if (it.status) {
                val members = it.toMembers()
                val name = it.name!!
                val code = it.code!!
                val adminEmail = it.admin!!

                callback.onSuccessful(GetLeague(name, code, adminEmail, members))
            } else {
                handleError(it.message!!, callback)
            }
        }
    }

    override suspend fun renameLeague(name: String, code: String, callback: RepoCallback<String>) {
        val json = Gson().toJson(RenameLeague(name, code))
        val call = client.getApiService().renameLeague(json)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.message)
            } else {
                handleError(it.message, callback)
            }
        }
    }

    override suspend fun leaveLeague(token: String?, callback: RepoCallback<String>) {
        val call = client.getApiService().leaveLeague(token!!)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.message)
            } else {
                handleError(it.message, callback)
            }
        }
    }

    override suspend fun updateProfile(
        token: String,
        image: String,
        callback: RepoCallback<String>
    ) {
        val call = client.getApiService().updateProfile(token, image)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.message)
            } else {
                handleError(it.message, callback)
            }
        }
    }
}