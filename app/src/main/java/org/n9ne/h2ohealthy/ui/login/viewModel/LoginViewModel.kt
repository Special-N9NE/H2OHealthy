package org.n9ne.h2ohealthy.ui.login.viewModel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.repo.auth.AuthRepo
import org.n9ne.h2ohealthy.util.Event
import org.n9ne.h2ohealthy.util.RepoCallback
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class LoginViewModel : ViewModel() {
    private var passwordIsVisible = false
    lateinit var navigator: Navigator
    val ldPasswordClick = MutableLiveData<Boolean>()


    val ldToken = MutableLiveData<Event<String>>()
    val ldError = MutableLiveData<Event<String>>()

    var repo: AuthRepo? = null

    fun passwordClick() {
        passwordIsVisible = !passwordIsVisible
        ldPasswordClick.postValue(passwordIsVisible)
    }

    fun login(email: String, password: String) {

        if (!isDataValid(email, password))
            return

        runBlocking {
            repo?.login(email, password, object : RepoCallback<String> {
                override fun onSuccessful(response: String) {
                    ldToken.postValue(Event(response))
                }

                override fun onError(error: String, isNetwork: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    private fun isDataValid(email: String, password: String): Boolean {
        if (email.trim().isEmpty()) {
            ldError.postValue(Event("Email is empty"))
            return false
        }
        if (password.trim().isEmpty()) {
            ldError.postValue(Event("Password is empty"))
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ldError.postValue(Event("Email is not valid"))
            return false
        }
        if (password.length < 6) {
            ldError.postValue(Event("password is short"))
            return false
        }

        return true
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