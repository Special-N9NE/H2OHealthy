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

class HomeViewModel(private val repo: HomeRepo) : ViewModel() {
    var target: Int? = null
    val activities = listOf(
        Activity("300", "2023/10/30", "3 minutes ago"),
        Activity("300", "2023/10/30", "3 minutes ago")
    )

    val ldTarget = MutableLiveData<Int>()
    val ldDayProgress = MutableLiveData<Int>()
    val ldWeekProgress = MutableLiveData<List<Progress>>()
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
