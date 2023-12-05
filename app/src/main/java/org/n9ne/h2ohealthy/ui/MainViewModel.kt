package org.n9ne.h2ohealthy.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.repo.MainRepo
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.util.DateUtils
import org.n9ne.h2ohealthy.util.Event
import org.n9ne.h2ohealthy.util.Mapper.toWater
import org.n9ne.h2ohealthy.util.RepoCallback

class MainViewModel : ViewModel() {

    var repo: MainRepo? = null
    var db: AppDatabase? = null

    val ldCups = MutableLiveData<List<Cup>>()
    val ldInsertWater = MutableLiveData<Event<Unit>>()

    val ldError = MutableLiveData<Event<String>>()

    fun insertWater(amount: String, token: String?) {
        val date = DateUtils.getDate()
        val time = DateUtils.getTime()

        val water = Activity(null, null, amount, date, time)

        viewModelScope.launch(Dispatchers.IO) {
            repo?.insertWater(water, token, object : RepoCallback<Long> {
                override fun onSuccessful(response: Long) {

                    water.id = response

                    syncWater(water)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    private fun syncWater(water: Activity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db?.roomDao()!!.insertWater(water.toWater())

                    ldInsertWater.postValue(Event(Unit))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getCups(token: String?) {

        viewModelScope.launch(Dispatchers.IO) {
            repo?.getCups(token, object : RepoCallback<List<Cup>> {
                override fun onSuccessful(response: List<Cup>) {
                    ldCups.postValue(response)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }

            })
        }
    }

}