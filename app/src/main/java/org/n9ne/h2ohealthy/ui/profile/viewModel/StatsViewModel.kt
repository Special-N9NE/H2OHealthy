package org.n9ne.h2ohealthy.ui.profile.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.common.model.Activity
import org.n9ne.common.util.Mapper.toWater
import org.n9ne.common.util.RepoCallback
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepo
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.util.Event

class StatsViewModel : ViewModel() {

    var repo: ProfileRepo? = null
    var db: AppDatabase? = null


    val ldBarData = MutableLiveData<Event<List<Double>>>()
    val ldActivities = MutableLiveData<Event<List<org.n9ne.common.model.Activity>>>()
    val ldError = MutableLiveData<Event<String>>()
    val ldToken = MutableLiveData<Event<Unit>>()


    fun getActivities(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.getAllActivity(token, object : RepoCallback<List<Activity>> {
                override fun onSuccessful(response: List<org.n9ne.common.model.Activity>) {

                    syncActivities(response)

                    val barValues = mutableListOf<Double>()
                    for (k in 0 until 24) {
                        var total = 0.0
                        response.forEach { j ->
                            val t2 = j.time.split(":")[0].toInt()
                            if (k == t2) {
                                total += j.amount.toDouble()
                            }
                        }

                        barValues.add(total)
                    }

                    val result = mutableListOf<Double>()
                    for (i in 0..(barValues.size - 2) step 2) {
                        result.add(barValues[i] + barValues[i + 1])
                    }

                    ldBarData.postValue(Event(result))
                    ldActivities.postValue(Event(response))

                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    private fun syncActivities(list: List<org.n9ne.common.model.Activity>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db?.roomDao()?.removeProgress()

                    list.forEach {
                        db?.roomDao()!!.insertWater(it.toWater())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}