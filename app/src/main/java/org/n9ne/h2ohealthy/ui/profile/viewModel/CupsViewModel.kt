package org.n9ne.h2ohealthy.ui.profile.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.repo.ProfileRepo
import org.n9ne.h2ohealthy.util.RepoCallback
import org.n9ne.h2ohealthy.util.Response

class CupsViewModel : ViewModel() {

    var repo: ProfileRepo? = null

    val ldShowDialog = MutableLiveData<Response<Boolean>>()
    val ldCups = MutableLiveData<List<Cup>>()
    val ldAddCup = MutableLiveData<Boolean>()
    val ldError = MutableLiveData<String>()


    fun addCupClick(@Suppress("UNUSED_PARAMETER") v: View) {
        ldCups.value?.let {
            if (it.size <= 5) {
                ldShowDialog.postValue(Response(true))
            } else {
                //TODO change error
                ldError.postValue("6 cups reached")
            }
        }
    }

    fun getCups() {
        repo?.getCups(object : RepoCallback<List<Cup>> {
            override fun onSuccessful(response: List<Cup>) {
                ldCups.postValue(response)
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(error)
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

                ldAddCup.postValue(true)
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(error)
            }

        })
    }
}