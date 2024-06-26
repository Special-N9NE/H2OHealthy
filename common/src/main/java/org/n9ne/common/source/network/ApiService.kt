package org.n9ne.common.source.network

import org.n9ne.common.source.objects.GetCups
import org.n9ne.common.source.objects.GetMembers
import org.n9ne.common.source.objects.GetProgress
import org.n9ne.common.source.objects.GetUser
import org.n9ne.common.source.objects.Login
import org.n9ne.common.source.objects.Message
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
    fun updateUser(
        @Header("Authorization") token: String,
        @Field("updateUser") data: String
    ): Call<GetUser>

    @FormUrlEncoded
    @POST("user.php")
    fun getUser(
        @Header("Authorization") token: String,
        @Field("getUser") data: String = ""
    ): Call<GetUser>

    @FormUrlEncoded
    @POST("glass.php")
    fun getCups(
        @Header("Authorization") token: String,
        @Field("getGlass") data: String = ""
    ): Call<GetCups>

    @FormUrlEncoded
    @POST("glass.php")
    fun addCup(
        @Header("Authorization") token: String,
        @Field("insertGlass") data: String
    ): Call<Message>

    @FormUrlEncoded
    @POST("glass.php")
    fun updateCup(
        @Field("update") data: String
    ): Call<Message>

    @FormUrlEncoded
    @POST("glass.php")
    fun removeCup(
        @Field("deleteGlass") data: String
    ): Call<Message>


    @FormUrlEncoded
    @POST("water.php")
    fun addActivity(
        @Field("insertWater") data: String,
        @Header("Authorization") token: String
    ): Call<Message>


    @FormUrlEncoded
    @POST("user.php")
    fun getTarget(
        @Header("Authorization") token: String,
        @Field("getTarget") data: String = ""
    ): Call<Message>

    @FormUrlEncoded
    @POST("water.php")
    fun getProgress(
        @Header("Authorization") token: String,
        @Field("getUserAmout") data: String = ""
    ): Call<GetProgress>

    @FormUrlEncoded
    @POST("water.php")
    fun updateActivity(
        @Header("Authorization") token: String,
        @Field("updateAmout") data: String
    ): Call<Message>

    @FormUrlEncoded
    @POST("water.php")
    fun removeActivity(
        @Header("Authorization") token: String,
        @Field("deleteWater") data: String
    ): Call<Message>

    @FormUrlEncoded
    @POST("user.php")
    fun joinLeague(
        @Header("Authorization") token: String,
        @Field("joinLeague") code: String
    ): Call<Message>

    @FormUrlEncoded
    @POST("league.php")
    fun createLeague(
        @Header("Authorization") token: String,
        @Field("insertLeague") name: String
    ): Call<Message>

    @FormUrlEncoded
    @POST("league.php")
    fun getLeague(
        @Header("Authorization") token: String,
        @Field("getLeague") name: String = ""
    ): Call<GetMembers>

    @FormUrlEncoded
    @POST("league.php")
    fun renameLeague(
        @Field("updateLeague") data: String
    ): Call<Message>

    @FormUrlEncoded
    @POST("user.php")
    fun leaveLeague(
        @Header("Authorization") token: String,
        @Field("leaveLeague") data: String = ""
    ): Call<Message>

    @FormUrlEncoded
    @POST("user.php")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Field("updateProfile") data: String
    ): Call<Message>

    @FormUrlEncoded
    @POST("user.php")
    fun updatePassword(
        @Field("updatePassword") data: String
    ): Call<Message>

    @FormUrlEncoded
    @POST("user.php")
    fun sendRecovery(
        @Field("sendRecovery") data: String
    ): Call<Message>
}
