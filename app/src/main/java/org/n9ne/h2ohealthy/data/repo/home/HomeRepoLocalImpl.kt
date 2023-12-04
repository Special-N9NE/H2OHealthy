package org.n9ne.h2ohealthy.data.repo.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.source.local.RoomDao
import org.n9ne.h2ohealthy.util.Mapper.toActivityList
import org.n9ne.h2ohealthy.util.RepoCallback

class HomeRepoLocalImpl(private val dao: RoomDao) : HomeRepo {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getTarget(callback: RepoCallback<Int>) {
        withContext(Dispatchers.IO) {
            val users = async { dao.getUser() }
            users.invokeOnCompletion {
                callback.onSuccessful(users.getCompleted()[0].target.toInt())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getProgress(callback: RepoCallback<List<Activity>>) {
        withContext(Dispatchers.IO) {
            val progress = async { dao.getProgress() }
            progress.invokeOnCompletion {
                val result = progress.getCompleted().toActivityList()

                callback.onSuccessful(result)
            }
        }
    }

    override suspend fun updateWater(id: Long, amount: String, callback: RepoCallback<Boolean>) {
        dao.updateWater(id, amount)
        callback.onSuccessful(true)
    }

    override suspend fun removeWater(id: Long, callback: RepoCallback<Boolean>) {
        dao.removeWater(id)
        callback.onSuccessful(true)
    }
}