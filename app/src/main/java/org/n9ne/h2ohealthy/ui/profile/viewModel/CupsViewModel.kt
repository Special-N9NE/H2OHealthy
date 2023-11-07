package org.n9ne.h2ohealthy.ui.profile.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.util.Response

class CupsViewModel : ViewModel() {

    val cups = listOf(
        Cup("My cup", 100, "#92A3FD"),
        Cup("bottle", 500, "#C58BF2"),
        Cup("My cup", 2000, "#9DCEFF"),
        Cup("My cup", 300, "#EEA4CE"),
        Cup("title", 400, "#FFFFFFFF"),
    )

    val ldAddClick = MutableLiveData<Response<Boolean>>()
    fun addCupCLick(@Suppress("UNUSED_PARAMETER") v: View) {
        ldAddClick.postValue(Response(true))
    }
}