package com.example.home.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.home.home.HomeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.common.model.Activity
import org.n9ne.common.model.Progress
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.util.Event
import org.n9ne.common.util.Mapper.toLiter
import org.n9ne.common.util.Mapper.toMilliLiter
import org.n9ne.common.util.Mapper.toWater
import org.n9ne.common.util.RepoCallback
import org.n9ne.common.util.Utils
import kotlin.math.roundToInt

class HomeViewModel : ViewModel() {
    var target: Int? = null
    var repo: HomeRepo? = null
    var db: AppDatabase? = null

    val ldTarget = MutableLiveData<Int>()
    val ldDayProgress = MutableLiveData<Int>()
    val ldWeekProgress = MutableLiveData<List<Progress>>()
    val ldActivities = MutableLiveData<List<org.n9ne.common.model.Activity>>()
    val ldError = MutableLiveData<Event<String>>()

    fun getTarget(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.getTarget(token, object : RepoCallback<Int> {
                override fun onSuccessful(response: Int) {
                    syncTarget(response)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    fun getProgress(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.getProgress(token, object : RepoCallback<List<Activity>> {
                override fun onSuccessful(response: List<org.n9ne.common.model.Activity>) {

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

                    syncProgress(response)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }


    fun updateWater(activity: org.n9ne.common.model.Activity, token: String?) {
        if (activity.amount.toDouble() >= 3) {
            ldError.postValue(Event("amount is too high"))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            repo?.updateWater(activity.id!!, activity.amount, token, object : RepoCallback<String> {
                override fun onSuccessful(response: String) {

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

                    val score = calculateScore(activities)
                    syncScore(score)
                    syncActivity(activity, false)

                    ldDayProgress.postValue(progress.roundToInt())
                    ldActivities.postValue(activities)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    fun removeWater(activity: org.n9ne.common.model.Activity, token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.removeWater(activity.id!!, token, object : RepoCallback<String> {
                override fun onSuccessful(response: String) {
                    val activities = ldActivities.value!!.toCollection(ArrayList())

                    activities.removeIf { it.id == activity.id }


                    val list = arrayListOf<org.n9ne.common.model.Activity>()
                    activities.forEach {
                        list.add(
                            org.n9ne.common.model.Activity(
                                it.id,
                                it.idUser,
                                (it.amount.toDouble().toLiter()).toString(),
                                it.date,
                                ""
                            )
                        )
                    }

                    val score = calculateScore(activities)
                    syncScore(score)
                    syncActivity(activity, true)

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

    private fun syncScore(score: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db?.roomDao()!!.updateScore(score.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun syncTarget(data: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db?.roomDao()!!.updateTarget(data)

                    target = data
                    ldTarget.postValue(data)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun syncActivity(data: org.n9ne.common.model.Activity, deleted: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    if (deleted) {
                        db?.roomDao()!!.removeWater(data.id!!)
                    } else {
                        db?.roomDao()!!.updateWater(data.id!!, data.amount)

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun syncProgress(list: List<org.n9ne.common.model.Activity>) {
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

    private fun calculateScore(list: List<org.n9ne.common.model.Activity>): Int {
        var total = 0.0
        list.forEach {
            total += it.amount.toDouble()
        }

        total = total.toLiter()
        return (total / 3).roundToInt()
    }
}