package org.n9ne.h2ohealthy.ui.home.viewModel

import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Progress

class HomeViewModel : ViewModel() {
    val target = 8
    val progress = 70
    val activities = listOf(
        Activity("300", "2023/10/30", "3 minutes ago"),
        Activity("300", "2023/10/30", "3 minutes ago")
    )
    val dailyProgress = listOf(
        Progress(32 , "Sat"),
        Progress(65 , "Sun"),
        Progress( 45, "Mon")
    )
}