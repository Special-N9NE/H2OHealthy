package org.n9ne.h2ohealthy.data.repo.profile

import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.source.network.Client
import org.n9ne.h2ohealthy.data.source.objects.GetUser
import org.n9ne.h2ohealthy.util.Mapper.toUser
import org.n9ne.h2ohealthy.util.Messages
import org.n9ne.h2ohealthy.util.RepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRepoApiImpl(private val client: Client) : ProfileRepo {

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

    override suspend fun getCups(callback: RepoCallback<List<Cup>>) {
    }

    override suspend fun addCup(cup: Cup, callback: RepoCallback<Long>) {
    }

    override suspend fun updateCup(cup: Cup, callback: RepoCallback<Unit>) {
    }

    override suspend fun removeCup(cup: Cup, callback: RepoCallback<Unit>) {
    }
}