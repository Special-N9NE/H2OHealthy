package org.n9ne.h2ohealthy.data.repo.profile

import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.util.RepoCallback

interface ProfileRepo {
    suspend fun getUser(callback: RepoCallback<User>)
    suspend fun getCups(callback: RepoCallback<List<Cup>>)
    suspend fun addCup(cup: Cup, callback: RepoCallback<Long>)
    suspend fun updateCup(cup: Cup, callback: RepoCallback<Unit>)
    suspend fun removeCup(cup: Cup, callback: RepoCallback<Unit>)
}