package org.n9ne.h2ohealthy.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.repo.HomeRepo
import org.n9ne.h2ohealthy.data.repo.local.WaterEntity
import org.n9ne.h2ohealthy.util.DateUtils
import org.n9ne.h2ohealthy.util.RepoCallback
import org.n9ne.h2ohealthy.util.Utils

class MainViewModel(private val repo: HomeRepo) : ViewModel() {

    val cups = listOf(
        Cup("My cup", 100, R.color.linearBlueEnd),
        Cup("bottle", 500, R.color.linearPurpleEnd),
        Cup("My cup", 2000, R.color.linearBlueStart),
        Cup("My cup", 300, R.color.linearPurpleStart),
        Cup("title", 400, R.color.white),
        Cup("My cup", 100, R.color.gray),
    )
    val ldInsertWater = MutableLiveData<Boolean>()
    val ldError = MutableLiveData<String>()

    fun insertWater(amount: String, id: Long) {
        val date = DateUtils.getDate()
        val time = DateUtils.getTime()

        val water = WaterEntity(id, date, amount, time)
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
