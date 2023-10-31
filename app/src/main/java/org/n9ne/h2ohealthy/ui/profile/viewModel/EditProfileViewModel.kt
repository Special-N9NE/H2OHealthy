package org.n9ne.h2ohealthy.ui.profile.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class EditProfileViewModel : ViewModel() {

    lateinit var navigator: Navigator
    val ldPickImage = MutableLiveData<Boolean>()
    val ldSubmit = MutableLiveData<Boolean>()

    val genders = arrayListOf("Male", "Female")
    val activityLevels = arrayListOf("Never", "Low", "Sometimes", "High", "Athlete")


    fun submitClick(@Suppress("UNUSED_PARAMETER") v: View) {
        //TODO validation
        ldSubmit.postValue(true)
    }

    fun dateClick(@Suppress("UNUSED_PARAMETER") v: View) {
        //TODO open date
    }

    fun profileClick(@Suppress("UNUSED_PARAMETER") v: View) {
        ldPickImage.postValue(true)
    }
}