package org.n9ne.auth.ui.viewModel

import android.content.Context
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.n9ne.auth.repo.AuthRepo
import org.n9ne.common.BaseViewModel
import org.n9ne.common.model.ActivityType
import org.n9ne.common.model.CompleteProfileResult
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.source.local.UserEntity
import org.n9ne.common.source.objects.Auth
import org.n9ne.common.util.Event
import org.n9ne.common.util.RepoCallback


class CompleteProfileViewModel : BaseViewModel<AuthRepo>() {
    val ldShowDate = MutableLiveData<Event<Unit>>()

    val genders = arrayListOf("Male", "Female")
    val activityLevels = arrayListOf(
        ActivityType.NEVER.text,
        ActivityType.LOW.text,
        ActivityType.SOMETIMES.text,
        ActivityType.HIGH.text,
        ActivityType.ATHLETE.text
    )

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
        val date = org.n9ne.common.util.DateUtils.getDate()
        val genderId = if (gender == "Male") 1 else 0
        var activityId = "0"
        activityLevels.forEachIndexed { index, s ->
            if (s == activity)
                activityId = index.toString()
        }

        val data = Auth.CompleteProfile(
            email, name.trim(), password, date, activityId, birthdate,
            weight, height, genderId.toString()
        )

        if (!isUserValid(data))
            return

        runBlocking {
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

    private fun isUserValid(data: Auth.CompleteProfile): Boolean {
        if (data.weight.trim().isEmpty()) {
            ldError.postValue(Event("Enter Weight"))
            return false
        }
        if (data.height.trim().isEmpty()) {
            ldError.postValue(Event("Enter Height"))
            return false
        }
        if (!data.weight.trim().isDigitsOnly()) {
            ldError.postValue(Event("Enter correct number for weight"))
            return false
        }
        if (!data.height.trim().isDigitsOnly()) {
            ldError.postValue(Event("Enter correct number for height"))
            return false
        }

        return true
    }

    private fun initDatabase(context: Context, data: Auth.CompleteProfile, id: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
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
    }

    fun dateClick() {
        ldShowDate.postValue(Event(Unit))
    }
}