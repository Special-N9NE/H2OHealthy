package org.n9ne.auth.ui.viewModel

import android.content.Context
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.n9ne.auth.repo.AuthRepo
import org.n9ne.common.BaseViewModel
import org.n9ne.common.R
import org.n9ne.common.model.ActivityType
import org.n9ne.common.model.CompleteProfileResult
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.source.local.UserEntity
import org.n9ne.common.source.objects.Auth
import org.n9ne.common.util.DateUtils
import org.n9ne.common.util.Event
import org.n9ne.common.util.RepoCallback
import org.n9ne.common.util.Utils
import org.n9ne.common.util.Utils.toEnglishNumbers
import javax.inject.Inject


@HiltViewModel
class CompleteProfileViewModel @Inject constructor() : BaseViewModel<AuthRepo>() {
    val ldShowDate = MutableLiveData<Event<Unit>>()

    fun getActivityLevels(): ArrayList<String> {
        val list = arrayListOf<String>()
        ActivityType.entries.forEach {
            list.add(ActivityType.getLocalizedText(it, Utils.getLocal()))
        }
        return list
    }

    val ldUserToken = MutableLiveData<Event<String>>()

    fun completeProfile(
        name: String,
        email: String,
        password: String,
        activity: String,
        gender: String,
        birthdate: String,
        weight: String,
        height: String,
        context: Context
    ) {
        val date = DateUtils.getDate().toEnglishNumbers()

        val maleText = if (Utils.isLocalPersian()) "مرد" else "Male"
        val genderId = if (gender == maleText) 1 else 0

        var activityId = 0
        ActivityType.entries.forEachIndexed { index, it ->
            val text = ActivityType.getLocalizedText(it, Utils.getLocal())
            if (text == activity) {
                activityId = index + 1
            }
        }

        val data = Auth.CompleteProfile(
            email,
            name.trim(),
            password,
            date,
            activityId.toString(),
            birthdate,
            weight,
            height,
            genderId.toString()
        )

        if (!isUserValid(data, context))
            return

        viewModelScope.launch(Dispatchers.IO) {
            repo?.completeProfile(data, object : RepoCallback<CompleteProfileResult> {
                override fun onSuccessful(response: CompleteProfileResult) {

                    initDatabase(context, data, response.id)
                    ldUserToken.postValue(Event(response.token))
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    private fun isUserValid(data: Auth.CompleteProfile, context: Context): Boolean {
        if (data.weight.trim().isEmpty()) {
            ldError.postValue(Event(context.getString(R.string.enterWeight)))
            return false
        }
        if (data.height.trim().isEmpty()) {
            ldError.postValue(Event(context.getString(R.string.enterHeight)))
            return false
        }
        if (!data.weight.trim().isDigitsOnly()) {
            ldError.postValue(Event(context.getString(R.string.errorWeight)))
            return false
        }
        if (!data.height.trim().isDigitsOnly()) {
            ldError.postValue(Event(context.getString(R.string.errorHeight)))
            return false
        }
        if (data.birthdate.trim().isEmpty()) {
            ldError.postValue(Event(context.getString(R.string.emptyDate)))
            return false
        }
        return true
    }

    private fun initDatabase(context: Context, data: Auth.CompleteProfile, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(context).removeDatabase()

            val user = UserEntity(
                id.toLong(), data.idActivity.toLong(), data.idLeague.toLong(),
                data.email, data.password,
                data.date, data.name,
                data.birthdate, data.weight,
                data.height, data.gender.toInt(),
                data.score, data.target, data.profile
            )
            AppDatabase.getDatabase(context).roomDao().insertUser(user)

        }
    }

    fun dateClick() {
        ldShowDate.postValue(Event(Unit))
    }
}