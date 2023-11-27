package org.n9ne.h2ohealthy.data.source.network

import org.n9ne.h2ohealthy.data.source.objects.Login
import org.n9ne.h2ohealthy.data.source.objects.Message
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("user.php")
    fun login(@Field("login") data: String): Call<Message>
}