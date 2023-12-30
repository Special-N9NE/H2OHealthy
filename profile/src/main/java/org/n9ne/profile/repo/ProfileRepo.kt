package org.n9ne.profile.repo

import org.n9ne.common.model.Activity
import org.n9ne.common.model.CreateLeague
import org.n9ne.common.model.Cup
import org.n9ne.common.model.GetLeague
import org.n9ne.common.model.UpdateUser
import org.n9ne.common.model.User
import org.n9ne.common.util.RepoCallback

interface ProfileRepo {
    suspend fun getAllActivity(token: String? = null, callback: RepoCallback<List<Activity>>)
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
    suspend fun leaveLeague(token: String?, callback: RepoCallback<String>) {}
    suspend fun updateProfile(token: String, image: String, callback: RepoCallback<String>) {}
}