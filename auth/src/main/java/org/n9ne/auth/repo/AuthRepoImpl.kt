package org.n9ne.auth.repo

import com.google.gson.Gson
import org.n9ne.common.BaseRepoImpl
import org.n9ne.common.model.CompleteProfileResult
import org.n9ne.common.model.LoginResult
import org.n9ne.common.source.network.Client
import org.n9ne.common.source.objects.Auth
import org.n9ne.common.source.objects.SendRecovery
import org.n9ne.common.util.Mapper.toUser
import org.n9ne.common.util.RepoCallback

class AuthRepoImpl(private val client: Client) : BaseRepoImpl(), AuthRepo {

    override suspend fun login(
        email: String,
        password: String,
        callback: RepoCallback<LoginResult>
    ) {
        val json = Gson().toJson(Auth.Login(email, password))
        val call = client.getApiService().login(json)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(
                    LoginResult(
                        it.user!![0].toUser(),
                        it.message
                    )
                )
            } else {
                handleError(it.message, callback)
            }
        }
    }


    override suspend fun register(
        name: String,
        email: String,
        password: String,
        callback: RepoCallback<Unit>
    ) {
        val json = Gson().toJson(Auth.Register(name, email, password))
        val call = client.getApiService().register(json)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(Unit)
            } else {
                handleError(it.message, callback)
            }
        }
    }


    override suspend fun completeProfile(
        data: Auth.CompleteProfile,
        callback: RepoCallback<CompleteProfileResult>
    ) {
        val json = Gson().toJson(data)
        val call = client.getApiService().completeProfile(json)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(
                    CompleteProfileResult(
                        it.id!!,
                        it.message
                    )
                )
            } else {
                handleError(it.message, callback)
            }
        }
    }

    override suspend fun sendRecovery(
        email: String,
        name: String,
        text: String,
        callback: RepoCallback<String>
    ) {
        val json = Gson().toJson(SendRecovery(email, name, text))
        val call = client.getApiService().sendRecovery(json)
        getResponse(call, callback).collect {
            if (it.status) {
                callback.onSuccessful(it.message)
            } else {
                handleError(it.message, callback)
            }
        }
    }
}