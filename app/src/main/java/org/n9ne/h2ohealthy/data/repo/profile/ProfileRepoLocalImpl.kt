package org.n9ne.h2ohealthy.data.repo.profile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.source.local.RoomDao
import org.n9ne.h2ohealthy.util.Mapper.toCupList
import org.n9ne.h2ohealthy.util.Mapper.toUser
import org.n9ne.h2ohealthy.util.RepoCallback

class ProfileRepoLocalImpl(private val dao: RoomDao) : ProfileRepo {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getUser(token: String?, callback: RepoCallback<User>) {
        runBlocking(Dispatchers.IO) {
            val users = async { dao.getUser() }
            users.invokeOnCompletion {
                val result = users.getCompleted()[0].toUser()
                callback.onSuccessful(result)
            }
        }
    }

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