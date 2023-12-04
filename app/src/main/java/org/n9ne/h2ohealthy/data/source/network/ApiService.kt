package org.n9ne.h2ohealthy.data.source.network

import org.n9ne.h2ohealthy.data.source.objects.GetUser
import org.n9ne.h2ohealthy.data.source.objects.Login
import org.n9ne.h2ohealthy.data.source.objects.Message
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("user.php")
    fun login(@Field("login") data: String): Call<Login>

    @FormUrlEncoded
    @POST("user.php")
    fun register(@Field("signup") data: String): Call<Message>

    @FormUrlEncoded
    @POST("user.php")
    fun completeProfile(@Field("insertUser") data: String): Call<Message>


    @FormUrlEncoded
    @POST("user.php")
    fun getUser(
        @Header("Authorization") token: String,
        @Field("getUser") data: String = ""
    ): Call<GetUser>

}