package org.n9ne.h2ohealthy.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.repo.MainRepo
import org.n9ne.h2ohealthy.util.DateUtils
import org.n9ne.h2ohealthy.util.Event
import org.n9ne.h2ohealthy.util.RepoCallback

class MainViewModel : ViewModel() {

    var repo: MainRepo? = null

    val ldCups = MutableLiveData<List<Cup>>()
    val ldInsertWater = MutableLiveData<Event<Unit>>()

    val ldError = MutableLiveData<Event<String>>()

    fun insertWater(amount: String, id: Long) {
        val date = DateUtils.getDate()
        val time = DateUtils.getTime()

        //TODO change id user
        val water = Activity(id, 1L, amount, date, time)
        repo?.insertWater(water, object : RepoCallback<Boolean> {
            override fun onSuccessful(response: Boolean) {
                ldInsertWater.postValue(Event(Unit))
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(Event(error))
            }
        })
    }

    fun getCups() {
        repo?.getCups(object : RepoCallback<List<Cup>> {
            override fun onSuccessful(response: List<Cup>) {
                ldCups.postValue(response)
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(Event(error))
            }

        })
    }

}