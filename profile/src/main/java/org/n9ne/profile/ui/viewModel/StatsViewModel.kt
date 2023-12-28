package org.n9ne.profile.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.common.BaseViewModel
import org.n9ne.common.model.Activity
import org.n9ne.common.util.Event
import org.n9ne.common.util.Mapper.toWater
import org.n9ne.common.util.RepoCallback
import org.n9ne.profile.repo.ProfileRepo

class StatsViewModel : BaseViewModel<ProfileRepo>() {

    val ldBarData = MutableLiveData<Event<List<Double>>>()
    val ldActivities = MutableLiveData<Event<List<Activity>>>()

    fun getActivities(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.getAllActivity(token, object : RepoCallback<List<Activity>> {
                override fun onSuccessful(response: List<Activity>) {

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

    private fun syncActivities(list: List<Activity>) {
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