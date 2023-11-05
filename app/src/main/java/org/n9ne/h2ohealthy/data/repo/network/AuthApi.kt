package org.n9ne.h2ohealthy.data.repo.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.n9ne.h2ohealthy.data.repo.objects.Login
import org.n9ne.h2ohealthy.data.repo.objects.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthApi {
    companion object {
        private var instance: AuthApi? = null
        fun getInstance(): AuthApi {
            if (instance == null)
                instance = AuthApi()
            return instance!!
        }
    }

    fun login(email: String, password: String): LiveData<Message> {
        val result = MutableLiveData<Message>()
        val call = Client.getInstance().getApiService().login(Login.Send(email, password))

        call.enqueue(object : Callback<Message> {
            override fun onResponse(
                call: Call<Message>,
                response: Response<Message>
            ) {
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return result
    }
}