package org.n9ne.h2ohealthy.ui.login.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.repo.network.AuthApi
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class LoginViewModel : ViewModel() {
    private var passwordIsVisible = false
    lateinit var navigator: Navigator
    val ldPasswordClick = MutableLiveData<Boolean>()

    fun passwordClick(@Suppress("UNUSED_PARAMETER") v: View) {
        passwordIsVisible = !passwordIsVisible
        ldPasswordClick.postValue(passwordIsVisible)
    }

    fun loginClick(@Suppress("UNUSED_PARAMETER") v: View) {
        //TODO validation

        AuthApi.getInstance().login("amir@gmail.come", "777")
    }

    fun googleClick(@Suppress("UNUSED_PARAMETER") v: View) {
        //TODO login with google
    }

    fun registerClick(@Suppress("UNUSED_PARAMETER") v: View) {
        navigator.shouldNavigate(R.id.login_to_register)
    }

    fun forgetPasswordClick(@Suppress("UNUSED_PARAMETER") v: View) {
        //TODO send recovery email or something
    }
}