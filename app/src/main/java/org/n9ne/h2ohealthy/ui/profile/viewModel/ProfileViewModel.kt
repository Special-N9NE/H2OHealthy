package org.n9ne.h2ohealthy.ui.profile.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.ActivityType
import org.n9ne.h2ohealthy.data.model.Setting
import org.n9ne.h2ohealthy.data.model.SettingItem
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.repo.ProfileRepo
import org.n9ne.h2ohealthy.data.repo.local.UserEntity
import org.n9ne.h2ohealthy.util.DateUtils
import org.n9ne.h2ohealthy.util.RepoCallback
import org.n9ne.h2ohealthy.util.Response
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class ProfileViewModel(private val repo: ProfileRepo) : ViewModel() {

    lateinit var navigator: Navigator
    val ldInLeague = MutableLiveData<Response<Boolean>>()
    val ldUser = MutableLiveData<User>()
    val ldError = MutableLiveData<String>()

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


    fun getUser() {
        repo.getUser(object : RepoCallback<UserEntity> {
            override fun onSuccessful(response: UserEntity) {

                val activityType = ActivityType.entries[response.idActivity.toInt()]
                val age = DateUtils.calculateAge(response.birthdate)
                val user = User(
                    response.id,
                    activityType,
                    response.idLeague,
                    response.email,
                    response.name,
                    age.toString(),
                    response.weight,
                    response.height,
                    response.score,
                    response.profile
                )

                ldUser.postValue(user)
            }

            override fun onError(error: String, isNetwork: Boolean) {
                ldError.postValue(error)
            }
        })
    }
}

@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(private val repo: ProfileRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
