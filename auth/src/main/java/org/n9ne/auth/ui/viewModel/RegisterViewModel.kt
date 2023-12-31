package org.n9ne.auth.ui.viewModel

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.n9ne.auth.R
import org.n9ne.auth.repo.AuthRepo
import org.n9ne.common.BaseViewModel
import org.n9ne.common.R.string
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

    fun register(name: String, email: String, password: String, context: Context) {

        if (!isDataValid(name, email, password, context))
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

    private fun isDataValid(
        name: String,
        email: String,
        password: String,
        context: Context
    ): Boolean {

        if (name.trim().isEmpty()) {
            ldError.postValue(Event(context.getString(string.emptyName)))
            return false
        }
        if (name.length <= 1) {
            ldError.postValue(Event(context.getString(string.errorName)))
            return false
        }
        if (email.trim().isEmpty()) {
            ldError.postValue(Event(context.getString(string.emptyEmail)))
            return false
        }
        if (password.trim().isEmpty()) {
            ldError.postValue(Event(context.getString(string.emptyPassword)))
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ldError.postValue(Event(context.getString(string.errorEmail)))
            return false
        }
        if (password.length < 6) {
            ldError.postValue(Event(context.getString(string.errorPassword)))
            return false
        }


        return true
    }
}