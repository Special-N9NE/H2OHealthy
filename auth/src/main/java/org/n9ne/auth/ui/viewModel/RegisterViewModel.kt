package org.n9ne.auth.ui.viewModel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.n9ne.auth.R
import org.n9ne.auth.repo.AuthRepo
import org.n9ne.common.BaseViewModel
import org.n9ne.common.util.Event
import org.n9ne.common.util.RepoCallback

class RegisterViewModel : BaseViewModel<AuthRepo>() {
    private var passwordIsVisible = false
    val ldPasswordClick = MutableLiveData<Boolean>()

    val ldRegister = MutableLiveData<Event<Unit>>()

    fun passwordClick() {
        passwordIsVisible = !passwordIsVisible
        ldPasswordClick.postValue(passwordIsVisible)
    }

    fun loginClick() {
        navigator?.shouldNavigate(R.id.register_to_login)
    }

    fun googleClick() {
        //TODO register with google
    }

    fun register(name: String, email: String, password: String) {

        if (!isDataValid(name, email, password))
            return

        viewModelScope.launch(Dispatchers.IO) {
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