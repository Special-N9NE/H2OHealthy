package org.n9ne.h2ohealthy.data.repo.profile

import org.n9ne.h2ohealthy.data.model.CreateLeague
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.model.GetLeague
import org.n9ne.h2ohealthy.data.model.UpdateUser
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.util.RepoCallback

interface ProfileRepo {
    suspend fun getUser(token: String? = null, callback: RepoCallback<User>)
    suspend fun getCups(token: String? = null, callback: RepoCallback<List<Cup>>)
    suspend fun addCup(cup: Cup, token: String?, callback: RepoCallback<Long>) {}
    suspend fun updateCup(cup: Cup, callback: RepoCallback<String>) {}
    suspend fun removeCup(cup: Cup, callback: RepoCallback<String>) {}
    suspend fun updateUser(date: UpdateUser, token: String?, callback: RepoCallback<User>) {}
    suspend fun joinLeague(code: String, token: String?, callback: RepoCallback<Long>) {}
    suspend fun createLeague(name: String, token: String?, callback: RepoCallback<CreateLeague>) {}
    suspend fun getLeagueUsers(token: String?, callback: RepoCallback<GetLeague>) {}
    suspend fun renameLeague(name: String, code: String, callback: RepoCallback<String>) {}
    suspend fun leaveLeague(token: String? , callback: RepoCallback<String>){}
}