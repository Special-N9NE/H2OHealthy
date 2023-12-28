package org.n9ne.profile.ui.viewModel

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.common.BaseViewModel
import org.n9ne.common.model.UpdateUser
import org.n9ne.common.model.User
import org.n9ne.common.util.Event
import org.n9ne.common.util.Mapper.toUserEntity
import org.n9ne.common.util.RepoCallback
import org.n9ne.profile.repo.ProfileRepo

class EditProfileViewModel : BaseViewModel<ProfileRepo>() {

    val ldSubmit = MutableLiveData<Event<Unit>>()
    val ldUser = MutableLiveData<Event<User>>()

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
       if (!isUserValid(user)) {
            return
        }

        var idActivity = 0
        val gender = if (user.gender == "Male") 1 else 0
        org.n9ne.common.model.ActivityType.entries.forEachIndexed { index, it ->
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

    private fun isUserValid(data: UpdateUser): Boolean {
        if (data.name.trim().isEmpty()) {
            ldError.postValue(Event("Name is empty"))
            return false
        }
        if (data.email.trim().isEmpty()) {
            ldError.postValue(Event("Email is empty"))
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(data.email).matches()) {
            ldError.postValue(Event("Email is not valid"))
            return false
        }
        if (data.name.length <= 1) {
            ldError.postValue(Event("name is short"))
            return false
        }
        if (data.weight.trim().isEmpty()) {
            ldError.postValue(Event("Enter Weight"))
            return false
        }
        if (data.height.trim().isEmpty()) {
            ldError.postValue(Event("Enter Height"))
            return false
        }
        if (data.birthdate.trim().isEmpty()) {
            ldError.postValue(Event("Enter Birthdate"))
            return false
        }
        if (!data.weight.trim().isDigitsOnly()) {
            ldError.postValue(Event("Enter correct number for weight"))
            return false
        }
        if (!data.height.trim().isDigitsOnly()) {
            ldError.postValue(Event("Enter correct number for height"))
            return false
        }

        return true
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
