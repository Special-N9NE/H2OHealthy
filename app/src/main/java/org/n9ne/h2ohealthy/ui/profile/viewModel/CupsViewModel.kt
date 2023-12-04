package org.n9ne.h2ohealthy.ui.profile.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepo
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.util.Event
import org.n9ne.h2ohealthy.util.Mapper.toGlass
import org.n9ne.h2ohealthy.util.RepoCallback

class CupsViewModel : ViewModel() {

    var repo: ProfileRepo? = null
    var db: AppDatabase? = null

    val ldCups = MutableLiveData<List<Cup>>()

    val ldShowDialog = MutableLiveData<Event<Boolean>>()
    val ldAddCup = MutableLiveData<Event<Unit>>()
    val ldRemoveCup = MutableLiveData<Event<Unit>>()
    val ldError = MutableLiveData<Event<String>>()


    fun addCupClick() {
        ldCups.value?.let {
            if (it.size <= 5) {
                ldShowDialog.postValue(Event(true))
            } else {
                //TODO change error
                ldError.postValue(Event("6 cups reached"))
            }
        }
    }

    fun getCups(token: String?) {

        viewModelScope.launch(Dispatchers.IO) {
            repo?.getCups(token, object : RepoCallback<List<Cup>> {
                override fun onSuccessful(response: List<Cup>) {

                    syncCups(response)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }

            })
        }
    }

    private fun syncCups(cups: List<Cup>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db?.roomDao()?.removeCups()

                    cups.forEach {
                        db?.roomDao()!!.insertCup(it.toGlass())
                    }

                    ldCups.postValue(cups)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun addCup(cup: Cup, token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.addCup(cup, token, object : RepoCallback<Long> {
                override fun onSuccessful(response: Long) {

                    val cups = ldCups.value!!.toCollection(ArrayList())
                    cup.id = response
                    cups.add(cup)

                    syncCups(cups)

                    ldAddCup.postValue(Event(Unit))
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }

            })
        }
    }

    fun updateCup(cup: Cup) {

        viewModelScope.launch(Dispatchers.IO) {
            repo?.updateCup(cup, object : RepoCallback<Unit> {
                override fun onSuccessful(response: Unit) {

                    val cups = ldCups.value!!.toCollection(ArrayList())
                    cups.forEach {
                        if (it.id == cup.id) {
                            it.title = cup.title
                            it.capacity = cup.capacity
                            it.color = cup.color
                        }
                    }
                    ldCups.postValue(cups)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }

            })
        }
    }

    fun removeCup(cup: Cup) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.removeCup(cup, object : RepoCallback<Unit> {
                override fun onSuccessful(response: Unit) {

                    val cups = ldCups.value!!.toCollection(ArrayList())
                    cups.removeIf { it.id == cup.id }

                    ldCups.postValue(cups)
                    ldRemoveCup.postValue(Event(Unit))
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }
}