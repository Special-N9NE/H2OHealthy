package org.n9ne.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking
import org.n9ne.common.util.Messages
import org.n9ne.common.util.RepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseRepoImpl {

    fun <T> handleError(error: String, callback: RepoCallback<T>) {
        if (error == "-1")
            callback.onError(error, isToken = true)
        else
            callback.onError(error)
    }

    fun <T, P> getResponse(
        call: Call<T>, callback: RepoCallback<P>
    ): Flow<T> {
        val responseFlow = MutableSharedFlow<T>()

        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val result = response.body()!!
                    runBlocking {
                        responseFlow.emit(result)
                    }
                } else {
                    val result = response.code().toString()
                    callback.onError(result)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (t.message?.contains(Messages.NO_INTERNET) == true) {
                    val result = Messages.errorNetwork
                    callback.onError(result, true)
                } else {
                    val result = t.message.toString()
                    t.printStackTrace()
                    callback.onError(result)
                }
            }
        })

        return responseFlow.asSharedFlow()
    }
}