package org.n9ne.h2ohealthy.ui.profile.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.n9ne.h2ohealthy.data.model.GetLeague
import org.n9ne.h2ohealthy.data.model.League
import org.n9ne.h2ohealthy.data.model.Member
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepo
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.util.Event
import org.n9ne.h2ohealthy.util.RepoCallback

class LeagueViewModel : ViewModel() {

    var repo: ProfileRepo? = null
    var db: AppDatabase? = null


    val ldLeague = MutableLiveData<Event<League>>()
    val ldMembers = MutableLiveData<Event<List<Member>>>()
    val ldError = MutableLiveData<Event<String>>()
    val ldToken = MutableLiveData<Event<Unit>>()


    fun getMembers(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.getLeagueUsers(token, object : RepoCallback<GetLeague> {
                override fun onSuccessful(response: GetLeague) {
                    val members = response.members.sortedBy { it.score }
                    val league =
                        League(null, null, response.adminEmail, response.name, response.code)

                    ldLeague.postValue(Event(league))
                    ldMembers.postValue(Event(members.asReversed()))
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    if (isToken) ldToken.postValue(Event(Unit))
                    else ldError.postValue(Event(error))
                }
            })
        }
    }


    fun renameLeague(name: String, code: String) {

        if (name.trim().isBlank()) {
            ldError.postValue(Event("enter name"))
            return
        }
        if (name.trim().length <= 2) {
            ldError.postValue(Event("name is too short"))
            return
        }
        if (!name.trim().matches("([A-Za-z0-9]+\\-*)".toRegex())) {
            ldError.postValue(Event("name can only be letters, digits and dashes"))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            repo?.renameLeague(name, code, object : RepoCallback<String> {
                override fun onSuccessful(response: String) {

                    val league = ldLeague.value!!.peekContent()
                    league.name = name

                    ldLeague.postValue(Event(league))
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    if (isToken) ldToken.postValue(Event(Unit))
                    else ldError.postValue(Event(error))
                }
            })
        }
    }
}