package org.n9ne.h2ohealthy.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.repo.local.RoomDao
import org.n9ne.h2ohealthy.util.Mapper.toActivityList
import org.n9ne.h2ohealthy.util.Mapper.toWater
import org.n9ne.h2ohealthy.util.RepoCallback

class HomeRepoLocalImpl(private val dao: RoomDao) : HomeRepo {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTarget(callback: RepoCallback<Int>) {
        runBlocking(Dispatchers.IO) {
            val users = async { dao.getUser() }
            users.invokeOnCompletion {
                callback.onSuccessful(users.getCompleted()[0].target.toInt())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getProgress(callback: RepoCallback<List<Activity>>) {
        runBlocking(Dispatchers.IO) {
            val progress = async { dao.getProgress() }
            progress.invokeOnCompletion {
                val result = progress.getCompleted().toActivityList()

                callback.onSuccessful(result)
            }
        }
    }

    override fun updateWater(id: Long, amount: String, callback: RepoCallback<Boolean>) {
        runBlocking(Dispatchers.IO) {
            dao.updateWater(id, amount)
            callback.onSuccessful(true)
        }
    }

    override fun removeWater(id: Long, callback: RepoCallback<Boolean>) {
        runBlocking(Dispatchers.IO) {
            dao.removeWater(id)
            callback.onSuccessful(true)
        }
    }
}