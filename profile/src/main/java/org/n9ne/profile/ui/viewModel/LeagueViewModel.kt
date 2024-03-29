package org.n9ne.profile.ui.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.common.BaseViewModel
import org.n9ne.common.R
import org.n9ne.common.model.GetLeague
import org.n9ne.common.model.League
import org.n9ne.common.model.Member
import org.n9ne.common.util.Event
import org.n9ne.common.util.RepoCallback
import org.n9ne.profile.repo.ProfileRepo
import javax.inject.Inject

@HiltViewModel
class LeagueViewModel @Inject constructor() : BaseViewModel<ProfileRepo>() {

    val ldLeave = MutableLiveData<Event<Unit>>()
    val ldLeague = MutableLiveData<Event<League>>()
    val ldMembers = MutableLiveData<Event<List<Member>>>()

    fun getMembers(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.getLeagueUsers(token, object : RepoCallback<GetLeague> {
                override fun onSuccessful(response: GetLeague) {
                    val members = response.members.sortedBy { it.score }
                    val league =
                        League(null, null, response.adminEmail, response.name, response.code)

                    ldMembers.postValue(Event(members.asReversed()))
                    ldLeague.postValue(Event(league))
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    if (isToken) ldToken.postValue(Event(Unit))
                    else ldError.postValue(Event(error))
                }
            })
        }
    }


    fun renameLeague(name: String, code: String, context: Context) {

        if (name.trim().isBlank()) {
            ldError.postValue(Event(context.getString(R.string.emptyName)))
            return
        }
        if (name.trim().length <= 2) {
            ldError.postValue(Event(context.getString(R.string.errorName)))
            return
        }
        if (!name.trim().matches("([A-Za-z0-9]+\\-*)".toRegex())) {
            ldError.postValue(Event(context.getString(R.string.errorLeagueName)))
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

    fun leave(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.leaveLeague(token, object : RepoCallback<String> {
                override fun onSuccessful(response: String) {
                    syncLeague()
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    if (isToken) ldToken.postValue(Event(Unit))
                    else ldError.postValue(Event(error))
                }
            })
        }
    }

    private fun syncLeague() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db?.roomDao()?.joinLeague(0L)
                    ldLeave.postValue(Event(Unit))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}