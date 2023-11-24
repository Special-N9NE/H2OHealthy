package org.n9ne.h2ohealthy.ui.profile.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.util.Event
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class EditProfileViewModel : ViewModel() {

    lateinit var navigator: Navigator
    val ldPickImage = MutableLiveData<Event<Unit>>()
    val ldSubmit = MutableLiveData<Event<Boolean>>()

    val genders = arrayListOf("Male", "Female")
    val activityLevels = arrayListOf("Never", "Low", "Sometimes", "High", "Athlete")


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