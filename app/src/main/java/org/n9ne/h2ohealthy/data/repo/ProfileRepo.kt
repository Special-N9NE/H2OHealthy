package org.n9ne.h2ohealthy.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.n9ne.h2ohealthy.data.repo.local.RoomDao
import org.n9ne.h2ohealthy.data.repo.local.UserEntity
import org.n9ne.h2ohealthy.util.RepoCallback

class ProfileRepo(private val dao: RoomDao) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getUser(callback: RepoCallback<UserEntity>) {
        runBlocking(Dispatchers.IO) {
            val users = async { dao.getUser() }
            users.invokeOnCompletion {
                callback.onSuccessful(users.getCompleted()[0])
            }
        }
    }
}