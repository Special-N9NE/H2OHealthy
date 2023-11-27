package org.n9ne.h2ohealthy.data.source.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
        val base = "https://kaizen-team.ir/h2oHealthy/"

        val interceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttp = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .client(okHttp)
            .baseUrl(base)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        apiService = retrofit.create(ApiService::class.java)
    }

    fun getApiService() = apiService
}