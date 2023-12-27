package org.n9ne.h2ohealthy.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.util.Event
import org.n9ne.common.util.Mapper.toWater
import org.n9ne.common.util.RepoCallback
import org.n9ne.common.model.Cup
import org.n9ne.h2ohealthy.data.repo.MainRepo

class MainViewModel : ViewModel() {

    var repo: MainRepo? = null
    var db: AppDatabase? = null

    val ldCups = MutableLiveData<List<Cup>>()
    val ldInsertWater = MutableLiveData<Event<Unit>>()

    val ldError = MutableLiveData<Event<String>>()

    fun insertWater(amount: String, token: String?) {
        if (amount.toDouble() >= 3) {
            ldError.postValue(Event("amount is too high"))
            return
        }

        val date = org.n9ne.common.util.DateUtils.getDate()
        val time = org.n9ne.common.util.DateUtils.getTime()

        val water = org.n9ne.common.model.Activity(null, null, amount, date, time)

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

    private fun syncWater(water: org.n9ne.common.model.Activity) {
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
        if (token == null)
            return

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