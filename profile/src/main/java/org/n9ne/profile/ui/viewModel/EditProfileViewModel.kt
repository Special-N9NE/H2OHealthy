package org.n9ne.profile.ui.viewModel

import android.content.Context
import android.util.Patterns
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.common.BaseViewModel
import org.n9ne.common.R
import org.n9ne.common.model.ActivityType
import org.n9ne.common.model.UpdateUser
import org.n9ne.common.model.User
import org.n9ne.common.util.DateUtils.georgianToPersian
import org.n9ne.common.util.Event
import org.n9ne.common.util.Mapper.toUserEntity
import org.n9ne.common.util.RepoCallback
import org.n9ne.common.util.Utils
import org.n9ne.profile.repo.ProfileRepo

class EditProfileViewModel : BaseViewModel<ProfileRepo>() {

    val ldSubmit = MutableLiveData<Event<Unit>>()
    val ldUser = MutableLiveData<Event<User>>()

    fun getActivityLevels(): ArrayList<String> {
        val list = arrayListOf<String>()
        ActivityType.entries.forEach {
            list.add(ActivityType.getLocalizedText(it, Utils.getLocal()))
        }
        return list
    }

    fun getGenders(): ArrayList<String> {
        return if (Utils.isLocalPersian())
            arrayListOf("مرد", "زن")
        else
            arrayListOf("Male", "Female")
    }

    fun getUser(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                repo?.getUser(token, object : RepoCallback<User> {
                    override fun onSuccessful(response: User) {

                        var gender = response.gender
                        var bithday = response.birthDate

                        if (Utils.isLocalPersian()) {
                            if (response.gender == "Male")
                                gender = "مرد"
                            else
                                gender = "زن"

                            bithday = bithday.georgianToPersian()
                        }

                        response.gender = gender
                        response.birthDate = bithday

                        ldUser.postValue(Event(response))
                    }

                    override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                        ldError.postValue(Event(error))
                    }
                })
            }
        }
    }

    fun saveData(user: UpdateUser, token: String?, context: Context) {
        if (!isUserValid(user, context)) {
            return
        }

        val gender = if (user.gender == getGenders()[0]) 1 else 0

        var idActivity = 0
        ActivityType.entries.forEachIndexed { index, it ->
            val text = ActivityType.getLocalizedText(it, Utils.getLocal())
            if (text == user.idActivity) {
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

    private fun isUserValid(data: UpdateUser, context: Context): Boolean {
        if (data.name.trim().isEmpty()) {
            ldError.postValue(Event(context.getString(R.string.emptyName)))
            return false
        }
        if (data.name.length <= 1) {
            ldError.postValue(Event(context.getString(R.string.errorName)))
            return false
        }

        if (data.email.trim().isEmpty()) {
            ldError.postValue(Event(context.getString(R.string.emptyEmail)))
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(data.email).matches()) {
            ldError.postValue(Event(context.getString(R.string.errorEmail)))
            return false
        }

        if (data.weight.trim().isEmpty()) {
            ldError.postValue(Event(context.getString(R.string.enterWeight)))
            return false
        }
        if (!data.weight.trim().isDigitsOnly()) {
            ldError.postValue(Event(context.getString(R.string.errorWeight)))
            return false
        }
        if (data.height.trim().isEmpty()) {
            ldError.postValue(Event(context.getString(R.string.enterHeight)))
            return false
        }
        if (!data.height.trim().isDigitsOnly()) {
            ldError.postValue(Event(context.getString(R.string.errorHeight)))
            return false
        }

        if (data.birthdate.trim().isEmpty()) {
            ldError.postValue(Event(context.getString(R.string.emptyDate)))
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
