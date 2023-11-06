package org.n9ne.h2ohealthy.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.n9ne.h2ohealthy.data.repo.local.RoomDao
import org.n9ne.h2ohealthy.data.repo.local.WaterEntity
import org.n9ne.h2ohealthy.util.RepoCallback

class HomeRepo(private val dao: RoomDao) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getTarget(callback: RepoCallback<Int>) {
        runBlocking(Dispatchers.IO) {
            val users = async { dao.getUser() }
            users.invokeOnCompletion {
                callback.onSuccessful(users.getCompleted()[0].target.toInt())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getProgress(callback: RepoCallback<List<WaterEntity>>) {
        runBlocking(Dispatchers.IO) {
            val progress = async { dao.getProgress() }
            progress.invokeOnCompletion {
                callback.onSuccessful(progress.getCompleted())
            }
        }
    }

    fun insertWater(water: WaterEntity, callback: RepoCallback<Boolean>) {
        runBlocking(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                dao.insertWater(water)
                callback.onSuccessful(true)
            }
        }
    }
}