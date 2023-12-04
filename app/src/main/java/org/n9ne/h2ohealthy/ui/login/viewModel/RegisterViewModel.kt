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

class RegisterViewModel : ViewModel() {
    private var passwordIsVisible = false
    lateinit var navigator: Navigator
    val ldPasswordClick = MutableLiveData<Boolean>()

    val ldRegister = MutableLiveData<Event<Unit>>()
    val ldError = MutableLiveData<Event<String>>()

    var repo: AuthRepo? = null

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

    fun register(name: String, email: String, password: String) {

        if (!isDataValid(name, email, password))
            return

        runBlocking {
            repo?.register(name.trim(), email, password, object : RepoCallback<Unit> {
                override fun onSuccessful(response: Unit) {
                    ldRegister.postValue(Event(Unit))
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }

            })
        }
    }

    private fun isDataValid(name: String, email: String, password: String): Boolean {
        if (name.trim().isEmpty()) {
            ldError.postValue(Event("Name is empty"))
            return false
        }
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
        if (name.length <= 1) {
            ldError.postValue(Event("name is short"))
            return false
        }
        if (password.length < 6) {
            ldError.postValue(Event("password is short"))
            return false
        }

        return true
    }
}