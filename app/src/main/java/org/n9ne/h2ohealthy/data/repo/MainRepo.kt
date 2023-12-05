package org.n9ne.h2ohealthy.data.repo

import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.util.RepoCallback

interface MainRepo {
    suspend fun insertWater(water: Activity, token: String?, callback: RepoCallback<Long>) {}
    suspend fun getCups(token: String? = null, callback: RepoCallback<List<Cup>>)

}