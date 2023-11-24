package org.n9ne.h2ohealthy.ui.profile.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.repo.ProfileRepo
import org.n9ne.h2ohealthy.util.Event
import org.n9ne.h2ohealthy.util.RepoCallback

class CupsViewModel : ViewModel() {

    var repo: ProfileRepo? = null

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

    fun addCup(cup: Cup) {
        repo?.addCup(cup, object : RepoCallback<Long> {
            override fun onSuccessful(response: Long) {

                val cups = ldCups.value!!.toCollection(ArrayList())
                cup.id = response
                cups.add(cup)

                ldCups.postValue(cups)

                ldAddCup.postValue(Event(Unit))
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(Event(error))
            }

        })
    }

    fun updateCup(cup: Cup) {
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

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(Event(error))
            }

        })
    }

    fun removeCup(cup: Cup) {
        repo?.removeCup(cup, object : RepoCallback<Unit> {
            override fun onSuccessful(response: Unit) {

                val cups = ldCups.value!!.toCollection(ArrayList())
                cups.removeIf { it.id == cup.id }

                ldCups.postValue(cups)
                ldRemoveCup.postValue(Event(Unit))
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(Event(error))
            }
        })
    }
}