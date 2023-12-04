package org.n9ne.h2ohealthy.ui.profile.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.Setting
import org.n9ne.h2ohealthy.data.model.SettingItem
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepo
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.util.DateUtils
import org.n9ne.h2ohealthy.util.Event
import org.n9ne.h2ohealthy.util.RepoCallback
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class ProfileViewModel : ViewModel() {

    var repo: ProfileRepo? = null

    lateinit var navigator: Navigator

    val ldLogout = MutableLiveData<Event<Unit>>()
    val ldInLeague = MutableLiveData<Event<Boolean>>()
    val ldUser = MutableLiveData<User>()
    val ldContactClick = MutableLiveData<Event<String>>()
    val ldError = MutableLiveData<Event<String>>()


    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.getUser(object : RepoCallback<User> {
                override fun onSuccessful(response: User) {

                    val age = DateUtils.calculateAge(response.birthDate)
                    response.age = age.toString()

                    ldUser.postValue(response)
                }

                override fun onError(error: String, isNetwork: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }


    val settings = listOf(
        Setting("Password", R.drawable.ic_password, SettingItem.PASSWORD),
        Setting("Activity History", R.drawable.ic_history, SettingItem.HISTORY),
        Setting("Workout Progress", R.drawable.ic_progress, SettingItem.PROGRESS),
        Setting("Glasses", R.drawable.ic_password, SettingItem.GLASS),
    )

    fun editClick() {
        navigator.shouldNavigate(R.id.profile_to_editProfile)
    }

    fun leagueClick() {
        //TODO check if user is joined in a league
        ldInLeague.postValue(Event(true))
    }

    fun contactUsClick() {
        //TODO change email
        ldContactClick.postValue(Event("abigdeli42@gmail.com"))
    }

    fun logout(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(context).removeDatabase()
            ldLogout.postValue(Event(Unit))
        }
    }

}