package org.n9ne.h2ohealthy.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.n9ne.common.BaseViewModel
import org.n9ne.common.util.Event
import org.n9ne.common.util.RepoCallback
import org.n9ne.common.util.Utils
import org.n9ne.h2ohealthy.data.MainRepo
import java.util.Base64

class ForgetViewModel : BaseViewModel<MainRepo>() {

    val ldRest = MutableLiveData<Event<String>>()

    fun resetPassword(password: String, email: String) {
        val error = if (Utils.isLocalPersian()) "رمز معتبر نیست" else "invalid password"
        if (password.length < 4) {
            ldError.postValue(Event(error))
            return
        }

        ldLoading.postValue(Event(true))

        viewModelScope.launch(Dispatchers.IO) {
            repo?.resetPassword(password, email, object : RepoCallback<Unit> {
                override fun onSuccessful(response: Unit) {
                    ldLoading.postValue(Event(false))

                    val text = if (Utils.isLocalPersian()) "موفقیت آمیز" else "successful"
                    ldRest.postValue(Event(text))
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                    ldLoading.postValue(Event(false))
                }
            })
        }
    }

    fun decodeEmail(email: String) =
        String(Base64.getDecoder().decode(email))

}