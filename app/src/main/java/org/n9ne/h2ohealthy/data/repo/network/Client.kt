package org.n9ne.h2ohealthy.data.repo.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.n9ne.h2ohealthy.util.Urls
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class Client private constructor() {
    companion object {
        lateinit var apiService: ApiService
        private var instance: Client? = null
        fun getInstance(): Client {
            if (instance == null) {
                instance = Client()
            }
            return instance!!
        }
    }

    init {
        val okHttp = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .client(okHttp)
            .baseUrl(Urls.base)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        apiService = retrofit.create(ApiService::class.java)
    }
    fun getApiService() = apiService
}