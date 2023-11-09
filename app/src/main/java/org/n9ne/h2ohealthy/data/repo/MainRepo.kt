package org.n9ne.h2ohealthy.data.repo

import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.util.RepoCallback

interface MainRepo {
    fun insertWater(water: Activity, callback: RepoCallback<Boolean>)

    fun getCups(callback: RepoCallback<List<Cup>>)
}