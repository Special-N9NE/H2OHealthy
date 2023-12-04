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

    val ldSubmit = MutableLiveData<Event<Unit>>()
    val ldUser = MutableLiveData<Event<User>>()
    val ldError = MutableLiveData<Event<String>>()

    var repo: ProfileRepo? = null

    val genders = arrayListOf("Male", "Female")
    val activityLevels = arrayListOf("Never", "Low", "Sometimes", "High", "Athlete")


    fun getUser(token: String? = null) {
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

    fun saveData() {
        //TODO validation
        ldSubmit.postValue(Event(Unit))
    }
}