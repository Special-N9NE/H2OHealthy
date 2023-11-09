package org.n9ne.h2ohealthy.ui.profile.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.Setting
import org.n9ne.h2ohealthy.data.model.SettingItem
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.repo.ProfileRepo
import org.n9ne.h2ohealthy.util.DateUtils
import org.n9ne.h2ohealthy.util.RepoCallback
import org.n9ne.h2ohealthy.util.Response
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class ProfileViewModel : ViewModel() {

    var repo: ProfileRepo? = null

    lateinit var navigator: Navigator
    val ldInLeague = MutableLiveData<Response<Boolean>>()
    val ldUser = MutableLiveData<User>()
    val ldError = MutableLiveData<String>()


    fun getUser() {
        repo?.getUser(object : RepoCallback<User> {
            override fun onSuccessful(response: User) {

                val age = DateUtils.calculateAge(response.birthDate)
                response.age = age.toString()

                ldUser.postValue(response)
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(error)
            }
        })
    }


    val settings = listOf(
        Setting("Password", R.drawable.ic_password, SettingItem.PASSWORD),
        Setting("Activity History", R.drawable.ic_history, SettingItem.HISTORY),
        Setting("Workout Progress", R.drawable.ic_progress, SettingItem.PROGRESS),
        Setting("Glasses", R.drawable.ic_password, SettingItem.GLASS),
    )

    fun editClick(@Suppress("UNUSED_PARAMETER") v: View) {
        navigator.shouldNavigate(R.id.profile_to_editProfile)
    }

    fun leagueClick(@Suppress("UNUSED_PARAMETER") v: View) {
        //TODO check if user is joined in a league
        ldInLeague.postValue(Response(true))
    }

    fun contactUsClick(@Suppress("UNUSED_PARAMETER") v: View) {
        //TODO validation
    }


}