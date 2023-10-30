package org.n9ne.h2ohealthy.ui

import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.Cup

class MainViewModel : ViewModel() {

    val cups = listOf(
        Cup("My cup", 100, R.color.linearBlueEnd),
        Cup("bottle", 500, R.color.linearPurpleEnd),
        Cup("My cup", 2000, R.color.linearBlueStart),
        Cup("My cup", 300, R.color.linearPurpleStart),
        Cup("title", 400, R.color.white),
        Cup("My cup", 100, R.color.gray),
    )
}