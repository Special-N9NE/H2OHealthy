package org.n9ne.h2ohealthy.data

import org.n9ne.common.model.Cup
import org.n9ne.common.util.RepoCallback

interface MainRepo {
    suspend fun insertWater(
        water: org.n9ne.common.model.Activity,
        token: String?,
        callback: RepoCallback<Long>
    ) {
    }

    suspend fun getCups(token: String? = null, callback: RepoCallback<List<Cup>>)

    suspend fun resetPassword(password: String, email: String, callback: RepoCallback<Unit>) {}

}