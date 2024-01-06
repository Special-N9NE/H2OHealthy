package org.n9ne.profile.ui.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.common.BaseViewModel
import org.n9ne.common.R.drawable
import org.n9ne.common.model.CreateLeague
import org.n9ne.common.model.League
import org.n9ne.common.model.Setting
import org.n9ne.common.model.SettingItem
import org.n9ne.common.model.User
import org.n9ne.common.util.Event
import org.n9ne.common.util.Mapper.toLeagueEntity
import org.n9ne.common.util.Mapper.toUserEntity
import org.n9ne.common.util.RepoCallback
import org.n9ne.common.util.Utils
import org.n9ne.profile.R
import org.n9ne.profile.repo.ProfileRepo

class ProfileViewModel : BaseViewModel<ProfileRepo>() {

    val ldJoinLeague = MutableLiveData<Event<Unit>>()

    val ldLogout = MutableLiveData<Event<Unit>>()
    val ldUser = MutableLiveData<User>()
    val ldContactClick = MutableLiveData<Event<String>>()

    fun getSettings(): List<Setting> {
        return if (Utils.isLocalPersian())
            listOf(
                Setting("آمار", drawable.ic_progress, SettingItem.STATS),
                Setting("لیوان ها", drawable.ic_cup_menu, SettingItem.GLASS),
                Setting("تعویض زبان", drawable.ic_language, SettingItem.LANGUAGE),
                Setting("یادآور", drawable.ic_language, SettingItem.REMINDER),
            )
        else
            listOf(
                Setting("Stats", drawable.ic_progress, SettingItem.STATS),
                Setting("Cups", drawable.ic_cup_menu, SettingItem.GLASS),
                Setting("Change Language", drawable.ic_language, SettingItem.LANGUAGE),
                Setting("Reminder", drawable.ic_language, SettingItem.REMINDER),
            )
    }

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
            val error = if (Utils.isLocalPersian())
                "کد را وارد کنید"
            else
                "enter code"
            ldError.postValue(Event(error))
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

    fun createLeague(name: String, token: String?, context: Context) {

        if (name.trim().isBlank()) {
            ldError.postValue(Event(context.getString(org.n9ne.common.R.string.emptyName)))
            return
        }
        if (name.trim().length <= 2) {
            ldError.postValue(Event(context.getString(org.n9ne.common.R.string.errorName)))
            return
        }
        if (!name.trim().matches("([A-Za-z0-9]+\\-*)".toRegex())) {
            ldError.postValue(Event(context.getString(org.n9ne.common.R.string.errorLeagueName)))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            repo?.createLeague(name, token, object : RepoCallback<CreateLeague> {
                override fun onSuccessful(response: CreateLeague) {

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
        navigator?.shouldNavigate(R.id.profile_to_editProfile)
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