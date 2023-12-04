package org.n9ne.h2ohealthy.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.source.local.RoomDao
import org.n9ne.h2ohealthy.util.Mapper.toCupList
import org.n9ne.h2ohealthy.util.Mapper.toWater
import org.n9ne.h2ohealthy.util.RepoCallback

class MainRepoLocalImpl(private val dao: RoomDao) : MainRepo {
    override suspend fun insertWater(water: Activity, callback: RepoCallback<Boolean>) {
        dao.insertWater(water.toWater())
        callback.onSuccessful(true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCups(callback: RepoCallback<List<Cup>>) {
        runBlocking(Dispatchers.IO) {
            val cups = async { dao.getCups() }
            cups.invokeOnCompletion {
                val result = cups.getCompleted().toCupList()
                callback.onSuccessful(result)
            }
        }
    }
}