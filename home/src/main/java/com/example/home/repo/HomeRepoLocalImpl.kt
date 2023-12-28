package com.example.home.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.n9ne.common.source.local.RoomDao
import org.n9ne.common.util.Mapper.toActivityList
import org.n9ne.common.util.RepoCallback

class HomeRepoLocalImpl(private val dao: RoomDao) : HomeRepo {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getTarget(token: String?, callback: RepoCallback<Int>) {
        withContext(Dispatchers.IO) {
            val users = async { dao.getUser() }
            users.invokeOnCompletion {
                if (users.getCompleted().isEmpty()) {
                    callback.onError("no user")
                } else
                    callback.onSuccessful(users.getCompleted()[0].target.toInt())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getProgress(token: String?, callback: RepoCallback<List<org.n9ne.common.model.Activity>>) {
        withContext(Dispatchers.IO) {
            val progress = async { dao.getProgress() }
            progress.invokeOnCompletion {
                val result = progress.getCompleted().toActivityList()

                callback.onSuccessful(result)
            }
        }
    }

    override suspend fun updateWater(
        id: Long,
        amount: String,
        token: String?,
        callback: RepoCallback<String>
    ) {
        dao.updateWater(id, amount)
        callback.onSuccessful("Success")
    }

    override suspend fun removeWater(id: Long, token: String?, callback: RepoCallback<String>) {
        dao.removeWater(id)
        callback.onSuccessful("Success")
    }
}