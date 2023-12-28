package org.n9ne.profile.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.common.R.drawable
import org.n9ne.common.model.CreateLeague
import org.n9ne.common.model.League
import org.n9ne.common.model.Setting
import org.n9ne.common.model.SettingItem
import org.n9ne.common.model.User
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.util.Event
import org.n9ne.common.util.Mapper.toLeagueEntity
import org.n9ne.common.util.Mapper.toUserEntity
import org.n9ne.common.util.RepoCallback
import org.n9ne.common.util.interfaces.Navigator
import org.n9ne.profile.R
import org.n9ne.profile.repo.ProfileRepo

class ProfileViewModel : ViewModel() {

    var repo: ProfileRepo? = null
    var db: AppDatabase? = null

    lateinit var navigator: Navigator


    val ldJoinLeague = MutableLiveData<Event<Unit>>()

    val ldLogout = MutableLiveData<Event<Unit>>()
    val ldUser = MutableLiveData<User>()
    val ldContactClick = MutableLiveData<Event<String>>()
    val ldError = MutableLiveData<Event<String>>()
    val ldToken = MutableLiveData<Event<Unit>>()

    val settings = listOf(
//        Setting("Password", R.drawable.ic_password, SettingItem.PASSWORD),
//        Setting("Stats", R.drawable.ic_progress, SettingItem.STATS),
        Setting("Glasses", drawable.ic_password, SettingItem.GLASS),
    )

    fun getUser(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.getUser(token, object : RepoCallback<User> {
                override fun onSuccessful(response: User) {
                    val age = org.n9ne.common.util.DateUtils.calculateAge(response.birthDate)
                    response.age = age.toString()

                    syncUser(response)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    if (isToken) ldToken.postValue(Event(Unit))
                    else ldError.postValue(Event(error))
                }
            })
        }
    }

    fun joinLeague(code: String, token: String?) {

        if (code.trim().isBlank()) {
            ldError.postValue(Event("enter code"))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            repo?.joinLeague(code, token, object : RepoCallback<Long> {
                override fun onSuccessful(response: Long) {

                    syncLeague(response, null, false)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    if (isToken) ldToken.postValue(Event(Unit))
                    else ldError.postValue(Event(error))
                }
            })
        }
    }

    fun createLeague(name: String, token: String?) {

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
            repo?.createLeague(name, token, object : RepoCallback<CreateLeague> {
                override fun onSuccessful(response: org.n9ne.common.model.CreateLeague) {

                    val league = League(response.id.toLong(), null, null, name, response.code)
                    syncLeague(league.id!!, league, true)
                    joinLeague(response.code, token)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    if (isToken) ldToken.postValue(Event(Unit))
                    else ldError.postValue(Event(error))
                }
            })
        }
    }

    private fun syncLeague(idLeague: Long, league: League?, created: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    if (created) {
                        db?.roomDao()?.removeLeagues()
                        db?.roomDao()?.insertLeague(league!!.toLeagueEntity())

                    } else {
                        db?.roomDao()?.joinLeague(idLeague)
                        ldJoinLeague.postValue(Event(Unit))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun syncUser(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db?.roomDao()!!.removeUser()
                    val data = user.toUserEntity()
                    db?.roomDao()?.insertUser(data)
                    ldUser.postValue(user)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun editClick() {
        navigator.shouldNavigate(R.id.profile_to_editProfile)
    }

    fun contactUsClick() {
        ldContactClick.postValue(Event("dev.kaizen.team@gmail.com"))
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            db?.removeDatabase()
            ldLogout.postValue(Event(Unit))
        }
    }

}