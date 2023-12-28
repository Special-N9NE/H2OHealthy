package org.n9ne.profile.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.n9ne.common.model.Activity
import org.n9ne.common.model.Cup
import org.n9ne.common.model.User
import org.n9ne.common.source.local.RoomDao
import org.n9ne.common.util.Mapper.toActivityList
import org.n9ne.common.util.Mapper.toCupList
import org.n9ne.common.util.Mapper.toUser
import org.n9ne.common.util.RepoCallback

class ProfileRepoLocalImpl(private val dao: RoomDao) : ProfileRepo {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllActivity(token: String?, callback: RepoCallback<List<Activity>>) {
        withContext(Dispatchers.IO) {
            val progress = async { dao.getProgress() }
            progress.invokeOnCompletion {
                val result = progress.getCompleted().toActivityList()

                callback.onSuccessful(result)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getUser(token: String?, callback: RepoCallback<User>) {
        runBlocking(Dispatchers.IO) {
            val users = async { dao.getUser() }
            users.invokeOnCompletion {
                if (users.getCompleted().isEmpty()) {
                    callback.onError("no user")
                } else {
                    val result = users.getCompleted()[0].toUser()
                    callback.onSuccessful(result)
                }
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