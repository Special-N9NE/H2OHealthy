package org.n9ne.h2ohealthy.ui.login.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import org.n9ne.h2ohealthy.data.model.ActivityType
import org.n9ne.h2ohealthy.data.repo.AuthRepo
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
        height: String
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
                    ldToken.postValue(Event(response))
                }

                override fun onError(error: String, isNetwork: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    fun dateClick() {
        //TODO open date dialog
    }
}