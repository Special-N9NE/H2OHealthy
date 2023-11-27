package org.n9ne.h2ohealthy.data.repo

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.n9ne.h2ohealthy.data.source.network.Client
import org.n9ne.h2ohealthy.data.source.objects.Login
import org.n9ne.h2ohealthy.data.source.objects.Message
import org.n9ne.h2ohealthy.util.Messages
import org.n9ne.h2ohealthy.util.RepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepoImpl(private val client: Client) : AuthRepo {

    override suspend fun login(email: String, password: String, callback: RepoCallback<String>) {
        withContext(Dispatchers.IO) {
            val json = Gson().toJson(Login.Send(email, password))
            client.getApiService().login(json)
                .enqueue(object : Callback<Message> {
                    override fun onResponse(
                        call: Call<Message>, response: Response<Message>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()!!

                            if (result.status) {
                                callback.onSuccessful(result.message)
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
}