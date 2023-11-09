package org.n9ne.h2ohealthy.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.repo.local.RoomDao
import org.n9ne.h2ohealthy.util.Mapper.toUser
import org.n9ne.h2ohealthy.util.RepoCallback

class ProfileRepoLocalImpl(private val dao: RoomDao) : ProfileRepo {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUser(callback: RepoCallback<User>) {
        runBlocking(Dispatchers.IO) {
            val users = async { dao.getUser() }
            users.invokeOnCompletion {
                val result = users.getCompleted()[0].toUser()
                callback.onSuccessful(result)
            }
        }
    }

    override fun getCups(callback: RepoCallback<List<Cup>>) {
        TODO("Not yet implemented")
    }

    override fun updateCup(cup: Cup, callback: RepoCallback<Boolean>) {
        TODO("Not yet implemented")
    }
}