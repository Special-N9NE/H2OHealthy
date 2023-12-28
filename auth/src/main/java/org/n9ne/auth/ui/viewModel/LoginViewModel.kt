package org.n9ne.auth.ui.viewModel

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.auth.R
import org.n9ne.auth.repo.AuthRepo
import org.n9ne.common.BaseViewModel
import org.n9ne.common.model.LoginResult
import org.n9ne.common.model.User
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.util.Event
import org.n9ne.common.util.Mapper.toUserEntity
import org.n9ne.common.util.RepoCallback

class LoginViewModel : BaseViewModel<AuthRepo>() {
    private var passwordIsVisible = false
    val ldPasswordClick = MutableLiveData<Boolean>()

    val ldName = MutableLiveData<Event<String>>()
    val ldUserToken = MutableLiveData<Event<String>>()

    fun passwordClick() {
        passwordIsVisible = !passwordIsVisible
        ldPasswordClick.postValue(passwordIsVisible)
    }

    fun login(email: String, password: String, context: Context) {

        if (!isDataValid(email, password))
            return

        viewModelScope.launch(Dispatchers.IO) {
            repo?.login(email, password, object : RepoCallback<LoginResult> {
                override fun onSuccessful(response: LoginResult) {
                    initDatabase(context, response.user)
                    ldUserToken.postValue(Event(response.token))
                    ldName.postValue(Event(response.user.name))
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
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

    private fun initDatabase(context: Context, data: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(context).removeDatabase()

                val user = data.toUserEntity()
                AppDatabase.getDatabase(context).roomDao().insertUser(user)
            }
        }
    }

    fun googleClick() {
        //TODO login with google
    }

    fun registerClick() {
        navigator?.shouldNavigate(R.id.login_to_register)
    }

    fun forgetPasswordClick() {
        //TODO send recovery email or something
    }
}