package org.n9ne.h2ohealthy.ui.profile.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.Member
import org.n9ne.h2ohealthy.util.Event

class LeagueViewModel : ViewModel() {

    private val members = listOf(
        Member(R.drawable.image_profile, "Narges", 4240L),
        Member(R.drawable.image_profile, "Amir Hossein", 992L),
        Member(R.drawable.image_profile, "Cristian", 25L),
        Member(R.drawable.image_profile, "random guy", 324L),
    )

    val ldSettingClick = MutableLiveData<Event<Boolean>>()

    fun settingClick() {
        ldSettingClick.postValue(Event(true))
    }

    fun getMembers(): List<Member> {
        return members.sortedBy {
            it.score
        }.asReversed()
    }
}