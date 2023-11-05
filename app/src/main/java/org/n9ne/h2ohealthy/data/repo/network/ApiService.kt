package org.n9ne.h2ohealthy.data.repo.network

import com.google.gson.JsonObject
import org.n9ne.h2ohealthy.data.repo.objects.Login
import org.n9ne.h2ohealthy.data.repo.objects.Message
import org.n9ne.h2ohealthy.util.Urls
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST(Urls.login)
    fun login(@Field("login") data: Login.Send): Call<Message>
}