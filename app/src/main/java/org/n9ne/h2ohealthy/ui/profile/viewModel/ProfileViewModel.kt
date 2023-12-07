package org.n9ne.h2ohealthy.ui.profile.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.CreateLeague
import org.n9ne.h2ohealthy.data.model.League
import org.n9ne.h2ohealthy.data.model.Setting
import org.n9ne.h2ohealthy.data.model.SettingItem
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepo
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.util.DateUtils
import org.n9ne.h2ohealthy.util.Event
import org.n9ne.h2ohealthy.util.Mapper.toLeagueEntity
import org.n9ne.h2ohealthy.util.Mapper.toUserEntity
import org.n9ne.h2ohealthy.util.RepoCallback
import org.n9ne.h2ohealthy.util.interfaces.Navigator

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
        Setting("Password", R.drawable.ic_password, SettingItem.PASSWORD),
        Setting("Activity History", R.drawable.ic_history, SettingItem.HISTORY),
        Setting("Workout Progress", R.drawable.ic_progress, SettingItem.PROGRESS),
        Setting("Glasses", R.drawable.ic_password, SettingItem.GLASS),
    )

    fun getUser(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.getUser(token, object : RepoCallback<User> {
                override fun onSuccessful(response: User) {
                    val age = DateUtils.calculateAge(response.birthDate)
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

        viewModelScope.launch(Dispatchers.IO) {
            repo?.createLeague(name, token, object : RepoCallback<CreateLeague> {
                override fun onSuccessful(response: CreateLeague) {

                    val league = League(response.id.toLong(), null, null,  name, response.code)
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
        //TODO change email
        ldContactClick.postValue(Event("abigdeli42@gmail.com"))
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            db?.removeDatabase()
            ldLogout.postValue(Event(Unit))
        }
    }

}