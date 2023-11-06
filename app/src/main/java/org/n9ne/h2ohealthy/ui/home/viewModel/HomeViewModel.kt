package org.n9ne.h2ohealthy.ui.home.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Progress
import org.n9ne.h2ohealthy.data.repo.HomeRepo
import org.n9ne.h2ohealthy.data.repo.local.WaterEntity
import org.n9ne.h2ohealthy.util.RepoCallback
import org.n9ne.h2ohealthy.util.Utils
import kotlin.math.roundToInt

class HomeViewModel(private val repo: HomeRepo) : ViewModel() {
    var target: Int? = null

    val ldTarget = MutableLiveData<Int>()
    val ldDayProgress = MutableLiveData<Int>()
    val ldWeekProgress = MutableLiveData<List<Progress>>()
    val ldActivities = MutableLiveData<List<Activity>>()
    val ldError = MutableLiveData<String>()

    fun getTarget() {
        repo.getTarget(object : RepoCallback<Int> {
            override fun onSuccessful(response: Int) {
                target = response
                ldTarget.postValue(response)
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(error)
            }
        })
    }

    fun getProgress() {
        repo.getProgress(object : RepoCallback<List<WaterEntity>> {
            override fun onSuccessful(response: List<WaterEntity>) {

                val dayProgress = Utils.calculateDayProgress(response)
                val progress = (100 * dayProgress) / target!!
                ldDayProgress.postValue(progress)

                val weekProgress = Utils.calculateWeekProgress(response)
                weekProgress.forEach {
                    it.progress = (100 * it.progress) / target!!
                }
                ldWeekProgress.postValue(weekProgress)

                ldActivities.postValue(Utils.calculateActivities(response))
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(error)
            }
        })
    }

    fun updateWater(activity: Activity) {

        repo.updateWater(activity.id, activity.amount, object : RepoCallback<Boolean> {
            override fun onSuccessful(response: Boolean) {

                val activities = ldActivities.value!!

                activities.forEach { item ->
                    if (item.id == activity.id) item.amount =
                        (activity.amount.toDouble() * 1000).roundToInt().toString()
                }

                var progress = 0.0
                activities.forEach { item ->
                    progress += item.amount.toDouble()
                }
                progress /= 1000
                progress = (100 * progress) / target!!
                ldDayProgress.postValue(progress.roundToInt())
                ldActivities.postValue(activities)
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(error)
            }
        })
    }

    fun removeWater(activity: Activity) {

        repo.removeWater(activity.id, object : RepoCallback<Boolean> {
            override fun onSuccessful(response: Boolean) {
                val activities = ldActivities.value!!.toCollection(ArrayList())

                activities.forEach { item ->
                    if (item.id == activity.id) activities.remove(item)
                }

                val list = arrayListOf<WaterEntity>()
                activities.forEach {
                    list.add(WaterEntity(it.id, it.date, it.amount, ""))
                }
                val progress = Utils.calculateDayProgress(list.toList())
                ldDayProgress.postValue(progress)
                ldActivities.postValue(activities)
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(error)
            }
        })
    }

}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val repo: HomeRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
