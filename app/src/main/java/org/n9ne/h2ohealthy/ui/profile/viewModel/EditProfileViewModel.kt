package org.n9ne.h2ohealthy.ui.profile.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepo
import org.n9ne.h2ohealthy.util.Event
import org.n9ne.h2ohealthy.util.RepoCallback
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class EditProfileViewModel : ViewModel() {

    lateinit var navigator: Navigator
    val ldPickImage = MutableLiveData<Event<Unit>>()
    val ldSubmit = MutableLiveData<Event<Boolean>>()
    val ldUser = MutableLiveData<Event<User>>()

    var repo: ProfileRepo? = null

    val genders = arrayListOf("Male", "Female")
    val activityLevels = arrayListOf("Never", "Low", "Sometimes", "High", "Athlete")


    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.getUser(object : RepoCallback<User> {
                override fun onSuccessful(response: User) {
                    ldUser.postValue(Event(response))
                }

                override fun onError(error: String, isNetwork: Boolean) {

                }
            })
        }
    }

    fun submitClick() {
        //TODO validation
        ldSubmit.postValue(Event(true))
    }

    fun dateClick() {
        //TODO open date
    }

    fun profileClick() {
        ldPickImage.postValue(Event(Unit))
    }
}