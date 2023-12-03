package org.n9ne.h2ohealthy.ui.login.viewModel

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.ActivityType
import org.n9ne.h2ohealthy.data.model.LoginResult
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.repo.auth.AuthRepo
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.data.source.local.UserEntity
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

    fun login(email: String, password: String, context: Context) {

        if (!isDataValid(email, password))
            return

        viewModelScope.launch(Dispatchers.IO) {
            repo?.login(email, password, object : RepoCallback<LoginResult> {
                override fun onSuccessful(response: LoginResult) {
                    initDatabase(context, response.user)
                    ldToken.postValue(Event(response.token))
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

    private fun initDatabase(context: Context, data: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(context).removeDatabase()

                var idActivity = 0
                ActivityType.entries.forEachIndexed { index, it ->
                    if (it == data.activityType)
                        idActivity = index
                }

                val gender = if (data.gender == "Male") 1 else 0

                val user = UserEntity(
                    idActivity.toLong(), data.idLeague,
                    data.email, data.password,
                    data.joinDate, data.name,
                    data.birthDate, data.weight,
                    data.height, gender,
                    data.score, data.target, data.profile
                )
                AppDatabase.getDatabase(context).roomDao().insertUser(user)
            }
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