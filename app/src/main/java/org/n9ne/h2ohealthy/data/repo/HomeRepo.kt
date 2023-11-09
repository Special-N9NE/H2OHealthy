package org.n9ne.h2ohealthy.data.repo

import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.util.RepoCallback

interface HomeRepo {
    fun getTarget(callback: RepoCallback<Int>)
    fun getProgress(callback: RepoCallback<List<Activity>>)
    fun insertWater(water: Activity, callback: RepoCallback<Boolean>)
    fun updateWater(id: Long, amount: String, callback: RepoCallback<Boolean>)
    fun removeWater(id: Long, callback: RepoCallback<Boolean>)
}