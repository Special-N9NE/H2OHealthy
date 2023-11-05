package org.n9ne.h2ohealthy.ui.home.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Progress
import org.n9ne.h2ohealthy.data.repo.HomeRepo
import org.n9ne.h2ohealthy.util.RepoCallback

class HomeViewModel(private val repo: HomeRepo) : ViewModel() {
    val progress = 70
    val activities = listOf(
        Activity("300", "2023/10/30", "3 minutes ago"),
        Activity("300", "2023/10/30", "3 minutes ago")
    )
    val dailyProgress = listOf(
        Progress(32, "Sat"),
        Progress(65, "Sun"),
        Progress(45, "Mon")
    )

    val ldTarget = MutableLiveData<Int>()
    val ldError = MutableLiveData<String>()

    fun getTarget() {
        repo.getTarget(object : RepoCallback<Int> {
            override fun onSuccessful(response: Int) {
                ldTarget.postValue(response)
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(error)
            }
        })
    }

}

class HomeViewModelFactory(private val repo: HomeRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
