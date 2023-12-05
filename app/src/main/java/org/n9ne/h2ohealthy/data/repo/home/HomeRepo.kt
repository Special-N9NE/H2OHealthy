package org.n9ne.h2ohealthy.data.repo.home

import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.util.RepoCallback

interface HomeRepo {
    suspend fun getTarget(token: String?, callback: RepoCallback<Int>)
    suspend fun getProgress(token: String?,callback: RepoCallback<List<Activity>>)
    suspend fun updateWater(id: Long, amount: String, callback: RepoCallback<String>) {}
    suspend fun removeWater(id: Long, callback: RepoCallback<String>) {}
}