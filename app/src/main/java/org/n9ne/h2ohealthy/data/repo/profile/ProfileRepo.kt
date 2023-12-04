package org.n9ne.h2ohealthy.data.repo.profile

import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.util.RepoCallback

interface ProfileRepo {
    suspend fun getUser(token: String? = null, callback: RepoCallback<User>)
    suspend fun getCups(token: String? = null, callback: RepoCallback<List<Cup>>)
    suspend fun addCup(cup: Cup, token: String? , callback: RepoCallback<Long>) {}
    suspend fun updateCup(cup: Cup, callback: RepoCallback<String>){}
    suspend fun removeCup(cup: Cup, callback: RepoCallback<String>){}
}