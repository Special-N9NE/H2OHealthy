package org.n9ne.h2ohealthy.ui.profile.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.h2ohealthy.data.model.ActivityType
import org.n9ne.h2ohealthy.data.model.UpdateUser
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepo
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.util.Event
import org.n9ne.h2ohealthy.util.Mapper.toUserEntity
import org.n9ne.h2ohealthy.util.RepoCallback
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class EditProfileViewModel : ViewModel() {

    lateinit var navigator: Navigator

    val ldSubmit = MutableLiveData<Event<Unit>>()
    val ldUser = MutableLiveData<Event<User>>()
    val ldError = MutableLiveData<Event<String>>()

    var repo: ProfileRepo? = null
    var db: AppDatabase? = null

    val genders = arrayListOf("Male", "Female")
    val activityLevels = arrayListOf("Never", "Low", "Sometimes", "High", "Athlete")


    fun getUser(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                repo?.getUser(token, object : RepoCallback<User> {
                    override fun onSuccessful(response: User) {
                        ldUser.postValue(Event(response))
                    }

                    override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                        ldError.postValue(Event(error))
                    }
                })
            }
        }
    }

    fun saveData(user: UpdateUser, token: String?) {
        //TODO validation
        //TODO save image

        var idActivity = 0
        val gender = if (user.gender == "Male") 1 else 0
        ActivityType.entries.forEachIndexed { index, it ->
            if (it.text == user.idActivity) {
                idActivity = index + 1
            }
        }

        user.gender = gender.toString()
        user.idActivity = idActivity.toString()

        viewModelScope.launch(Dispatchers.IO) {
            repo?.updateUser(user, token, object : RepoCallback<User> {
                override fun onSuccessful(response: User) {
                    syncUser(response)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    private fun syncUser(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db?.roomDao()!!.removeUser()
                    val data = user.toUserEntity()
                    db?.roomDao()?.insertUser(data)

                    ldSubmit.postValue(Event(Unit))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
