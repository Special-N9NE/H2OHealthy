package org.n9ne.h2ohealthy.ui.home.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Progress
import org.n9ne.h2ohealthy.data.repo.home.HomeRepo
import org.n9ne.h2ohealthy.util.Event
import org.n9ne.h2ohealthy.util.Mapper.toLiter
import org.n9ne.h2ohealthy.util.Mapper.toMilliLiter
import org.n9ne.h2ohealthy.util.RepoCallback
import org.n9ne.h2ohealthy.util.Utils
import kotlin.math.roundToInt

class HomeViewModel : ViewModel() {
    var target: Int? = null
    var repo: HomeRepo? = null

    val ldTarget = MutableLiveData<Int>()
    val ldDayProgress = MutableLiveData<Int>()
    val ldWeekProgress = MutableLiveData<List<Progress>>()
    val ldActivities = MutableLiveData<List<Activity>>()
    val ldError = MutableLiveData<Event<String>>()

    fun getTarget() {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.getTarget(object : RepoCallback<Int> {
                override fun onSuccessful(response: Int) {
                    target = response
                    ldTarget.postValue(response)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    fun getProgress() {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.getProgress(object : RepoCallback<List<Activity>> {
                override fun onSuccessful(response: List<Activity>) {

                    val dayProgress = Utils.calculateDayProgress(response)
                    val progress = (100 * dayProgress) / (target!!.toDouble().toMilliLiter())
                    ldDayProgress.postValue(progress.toInt())

                    val weekProgress = Utils.calculateWeekProgress(response)
                    weekProgress.forEach {
                        it.progress =
                            (100 * it.progress) / (target!!.toDouble().toMilliLiter().toInt())
                    }
                    ldWeekProgress.postValue(weekProgress)

                    ldActivities.postValue(Utils.calculateActivities(response))
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    fun updateWater(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.updateWater(activity.id!!, activity.amount, object : RepoCallback<Boolean> {
                override fun onSuccessful(response: Boolean) {

                    val activities = ldActivities.value!!

                    activities.forEach { item ->
                        if (item.id == activity.id) item.amount =
                            activity.amount.toDouble().toMilliLiter().roundToInt().toString()
                    }

                    var progress = 0.0
                    activities.forEach { item ->
                        progress += (item.amount.toDouble())
                    }
                    progress = (100 * progress) / (target!!.toDouble().toMilliLiter())
                    ldDayProgress.postValue(progress.roundToInt())
                    ldActivities.postValue(activities)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    fun removeWater(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.removeWater(activity.id!!, object : RepoCallback<Boolean> {
                override fun onSuccessful(response: Boolean) {
                    val activities = ldActivities.value!!.toCollection(ArrayList())

                    activities.removeIf { it.id == activity.id }

                    val list = arrayListOf<Activity>()
                    activities.forEach {
                        list.add(
                            Activity(
                                it.id,
                                it.idUser,
                                (it.amount.toDouble().toLiter()).toString(),
                                it.date,
                                ""
                            )
                        )
                    }
                    var progress = Utils.calculateDayProgress(list.toList())
                    progress = (100 * progress) / (target!!.toDouble().toMilliLiter().toInt())
                    ldDayProgress.postValue(progress)
                    ldActivities.postValue(activities)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }
}