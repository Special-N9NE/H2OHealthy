package org.n9ne.h2ohealthy.ui.login.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.repo.AuthRepo
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
        //TODO validation

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