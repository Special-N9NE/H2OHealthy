package org.n9ne.h2ohealthy.ui.login.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.repo.AuthRepo
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
        //TODO validation

        runBlocking {
            repo?.register(name, email, password, object : RepoCallback<Unit> {
                override fun onSuccessful(response: Unit) {
                    ldRegister.postValue(Event(Unit))
                }

                override fun onError(error: String, isNetwork: Boolean) {
                    ldError.postValue(Event(error))
                }

            })
        }
    }
}