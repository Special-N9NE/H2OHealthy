package org.n9ne.h2ohealthy.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.n9ne.common.model.Cup
import org.n9ne.common.source.local.RoomDao
import org.n9ne.common.util.Mapper.toCupList
import org.n9ne.common.util.RepoCallback

class MainRepoLocalImpl(private val dao: RoomDao) : MainRepo {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCups(token: String?, callback: RepoCallback<List<Cup>>) {
        runBlocking(Dispatchers.IO) {
            val cups = async { dao.getCups() }
            cups.invokeOnCompletion {
                val result = cups.getCompleted().toCupList()
                callback.onSuccessful(result)
            }
        }
    }
}