package org.n9ne.auth.ui.viewModel

import android.content.Context
import android.os.Build
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.auth.R
import org.n9ne.auth.repo.AuthRepo
import org.n9ne.common.BaseViewModel
import org.n9ne.common.R.string
import org.n9ne.common.model.LoginResult
import org.n9ne.common.model.User
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.util.Event
import org.n9ne.common.util.Mapper.toUserEntity
import org.n9ne.common.util.RepoCallback
import org.n9ne.common.util.Utils
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel<AuthRepo>() {
    private var passwordIsVisible = false
    private val ldPasswordClick = MutableLiveData<Boolean>()

    val ldName = MutableLiveData<Event<String>>()
    val ldUserToken = MutableLiveData<Event<String>>()
    val ldRecovery = MutableLiveData<Event<String>>()

    fun passwordClick() {
        passwordIsVisible = !passwordIsVisible
        ldPasswordClick.postValue(passwordIsVisible)
    }

    fun login(email: String, password: String, context: Context) {

        if (!isDataValid(email, password, context))
            return

        val encryptPass = Utils.encrypt(password)

        viewModelScope.launch(Dispatchers.IO) {
            repo?.login(email, encryptPass, object : RepoCallback<LoginResult> {
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

    private fun isDataValid(email: String, password: String, context: Context): Boolean {
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

    fun forgetPassword(email: String) {
        val error = if (Utils.isLocalPersian()) "ایمیل معتبر نیست" else "invalid email"
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ldError.postValue(Event(error))
            return
        }

        val encodedEmail = encodeEmail(email)
        val content = if (Utils.isLocalPersian())
            "کاربر گرامی\n " +
                    "برای بازنشانی رمزعبور روی لینک زیر کلیک کنید" +
                    "\n\n" +
                    " https://h2oforget/${encodedEmail}"
        else
            "Dear user\n " +
                    "Click link below to reset your password" +
                    "\n\n" +
                    "https://h2oforget/${encodedEmail}"

        viewModelScope.launch(Dispatchers.IO) {
            repo?.sendRecovery(email, email.split("@")[0], content, object : RepoCallback<String> {
                override fun onSuccessful(response: String) {
                    ldRecovery.postValue(Event(response))
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }

            })
        }
    }

    private fun encodeEmail(email: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(email.toByteArray());
        } else {
            android.util.Base64.encodeToString(
                email.toByteArray(),
                android.util.Base64.DEFAULT
            );
        }
    }
}