package org.n9ne.h2ohealthy.ui.login.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class CompleteProfileViewModel : ViewModel() {
    lateinit var navigator: Navigator

    val genders = arrayListOf("Male", "Female")
    val activityLevels = arrayListOf("Never", "Low", "Sometimes", "High", "Athlete")

    fun nextClick(@Suppress("UNUSED_PARAMETER") v: View) {
        //TODO validation
    }

    fun dateClick(@Suppress("UNUSED_PARAMETER") v: View) {
        //TODO open date dialog
    }
}