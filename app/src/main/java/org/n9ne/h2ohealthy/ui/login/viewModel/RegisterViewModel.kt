package org.n9ne.h2ohealthy.ui.login.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class RegisterViewModel : ViewModel() {
    private var passwordIsVisible = false
    lateinit var navigator: Navigator
    val ldPasswordClick = MutableLiveData<Boolean>()

    fun passwordClick() {
        passwordIsVisible = !passwordIsVisible
        ldPasswordClick.postValue(passwordIsVisible)
    }

    fun loginClick() {
        navigator.shouldNavigate(R.id.register_to_login)
    }

    fun googleClick() {
        //TODO register with google
    }

    fun registerClick() {
        //TODO validation
    }
}