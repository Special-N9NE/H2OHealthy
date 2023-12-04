package org.n9ne.h2ohealthy.data.repo.auth

import com.google.gson.Gson
import org.n9ne.h2ohealthy.data.model.CompleteProfileResult
import org.n9ne.h2ohealthy.data.model.LoginResult
import org.n9ne.h2ohealthy.data.source.network.Client
import org.n9ne.h2ohealthy.data.source.objects.Auth
import org.n9ne.h2ohealthy.data.source.objects.Login
import org.n9ne.h2ohealthy.data.source.objects.Message
import org.n9ne.h2ohealthy.util.Mapper.toUser
import org.n9ne.h2ohealthy.util.Messages
import org.n9ne.h2ohealthy.util.RepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepoImpl(private val client: Client) : AuthRepo {

    override suspend fun login(
        email: String,
        password: String,
        callback: RepoCallback<LoginResult>
    ) {
        val json = Gson().toJson(Auth.Login(email, password))
        client.getApiService().login(json)
            .enqueue(object : Callback<Login> {
                override fun onResponse(
                    call: Call<Login>, response: Response<Login>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!

                        if (result.status) {
                            callback.onSuccessful(
                                LoginResult(
                                    result.user!![0].toUser(),
                                    result.message
                                )
                            )
                        } else {
                            callback.onError(result.message)
                        }

                    } else {
                        val result = response.code().toString()
                        callback.onError(result)
                    }
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {

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


    override suspend fun register(
        name: String,
        email: String,
        password: String,
        callback: RepoCallback<Unit>
    ) {
        val json = Gson().toJson(Auth.Register(name, email, password))
        client.getApiService().register(json)
            .enqueue(object : Callback<Message> {
                override fun onResponse(
                    call: Call<Message>, response: Response<Message>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!

                        if (result.status) {
                            callback.onSuccessful(Unit)
                        } else {
                            callback.onError(result.message)
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


    override suspend fun completeProfile(
        data: Auth.CompleteProfile,
        callback: RepoCallback<CompleteProfileResult>
    ) {
        val json = Gson().toJson(data)
        client.getApiService().completeProfile(json)
            .enqueue(object : Callback<Message> {
                override fun onResponse(
                    call: Call<Message>, response: Response<Message>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!

                        if (result.status) {
                            callback.onSuccessful(
                                CompleteProfileResult(
                                    result.id!!,
                                    result.message
                                )
                            )
                        } else {
                            callback.onError(result.message)
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