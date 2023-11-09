package org.n9ne.h2ohealthy.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.repo.HomeRepo
import org.n9ne.h2ohealthy.util.DateUtils
import org.n9ne.h2ohealthy.util.RepoCallback

class MainViewModel(private val repo: HomeRepo) : ViewModel() {

    val cups = listOf<Cup>()
    val ldInsertWater = MutableLiveData<Boolean>()
    val ldError = MutableLiveData<String>()

    fun insertWater(amount: String, id: Long) {
        val date = DateUtils.getDate()
        val time = DateUtils.getTime()

        //TODO change id user
        val water = Activity(id, 1L, amount, date, time)
        repo.insertWater(water, object : RepoCallback<Boolean> {
            override fun onSuccessful(response: Boolean) {
                ldInsertWater.postValue(response)
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(error)
            }
        })
    }

}

class MainViewModelFactory(private val repo: HomeRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
