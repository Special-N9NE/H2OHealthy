package org.n9ne.h2ohealthy.data.repo.profile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.source.local.RoomDao
import org.n9ne.h2ohealthy.util.Mapper.toCupList
import org.n9ne.h2ohealthy.util.Mapper.toGlass
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

    override suspend fun updateUser(user: User, callback: RepoCallback<Unit>) {

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

    override suspend fun addCup(cup: Cup, callback: RepoCallback<Long>) {
        val id = dao.insertCup(cup.toGlass())
        callback.onSuccessful(id)
    }

    override suspend fun updateCup(cup: Cup, callback: RepoCallback<Unit>) {
        dao.updateCup(cup.id!!, cup.title, cup.capacity.toString(), cup.color)
        callback.onSuccessful(Unit)
    }

    override suspend fun removeCup(cup: Cup, callback: RepoCallback<Unit>) {
        dao.removeCup(cup.id!!)
        callback.onSuccessful(Unit)
    }
}