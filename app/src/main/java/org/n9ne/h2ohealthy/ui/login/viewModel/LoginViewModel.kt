package org.n9ne.h2ohealthy.ui.login.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.repo.network.AuthApi
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class LoginViewModel : ViewModel() {
    private var passwordIsVisible = false
    lateinit var navigator: Navigator
    val ldPasswordClick = MutableLiveData<Boolean>()

    fun passwordClick() {
        passwordIsVisible = !passwordIsVisible
        ldPasswordClick.postValue(passwordIsVisible)
    }

    fun loginClick() {
        //TODO validation

        AuthApi.getInstance().login("amir@gmail.come", "777")
    }

    fun googleClick() {
        //TODO login with google
    }

    fun registerClick() {
        navigator.shouldNavigate(R.id.login_to_register)
    }

    fun forgetPasswordClick() {
        //TODO send recovery email or something
    }
}