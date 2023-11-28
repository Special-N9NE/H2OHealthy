package org.n9ne.h2ohealthy.ui.login.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.n9ne.h2ohealthy.data.model.ActivityType
import org.n9ne.h2ohealthy.data.repo.auth.AuthRepo
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.data.source.local.UserEntity
import org.n9ne.h2ohealthy.data.source.objects.Auth
import org.n9ne.h2ohealthy.util.DateUtils
import org.n9ne.h2ohealthy.util.Event
import org.n9ne.h2ohealthy.util.RepoCallback
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class CompleteProfileViewModel : ViewModel() {
    lateinit var navigator: Navigator

    var repo: AuthRepo? = null

    val ldToken = MutableLiveData<Event<String>>()
    val ldError = MutableLiveData<Event<String>>()

    val genders = arrayListOf("Male", "Female")
    val activityLevels = arrayListOf(
        ActivityType.NEVER.text,
        ActivityType.LOW.text,
        ActivityType.SOMETIMES.text,
        ActivityType.HIGH.text,
        ActivityType.ATHLETE.text
    )

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
        //TODO validation

        val date = DateUtils.getDate()
        val genderId = if (gender == "Male") 1 else 0
        var activityId = "0"

        activityLevels.forEachIndexed { index, s ->
            if (s == activity)
                activityId = index.toString()
        }

        val data = Auth.CompleteProfile(
            email, name, password, date, activityId, birthdate,
            weight, height, genderId.toString()
        )

        runBlocking {
            repo?.completeProfile(data, object : RepoCallback<String> {
                override fun onSuccessful(response: String) {

                    initDatabase(context, data)
                    ldToken.postValue(Event(response))
                }

                override fun onError(error: String, isNetwork: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    private fun initDatabase(context: Context, data: Auth.CompleteProfile) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val user = UserEntity(
                    data.idActivity.toLong(), data.idLeague.toLong(),
                    data.email, data.password,
                    data.date, data.name,
                    data.birthdate, data.weight.toInt(),
                    data.height.toInt(), data.gender.toInt(),
                    data.score.toInt(), data.target, data.profile
                )
                AppDatabase.getDatabase(context).roomDao().insertUser(user)
            }
        }
    }

    fun dateClick() {
        //TODO open date dialog
    }
}