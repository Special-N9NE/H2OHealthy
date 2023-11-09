package org.n9ne.h2ohealthy.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.n9ne.h2ohealthy.data.repo.local.RoomDao
import org.n9ne.h2ohealthy.data.repo.local.WaterEntity
import org.n9ne.h2ohealthy.util.RepoCallback

interface HomeRepo{
    fun getTarget(callback: RepoCallback<Int>)
    fun getProgress(callback: RepoCallback<List<WaterEntity>>)
    fun insertWater(water: WaterEntity, callback: RepoCallback<Boolean>)
    fun updateWater(id: Long, amount: String, callback: RepoCallback<Boolean>)
    fun removeWater(id: Long, callback: RepoCallback<Boolean>)
}