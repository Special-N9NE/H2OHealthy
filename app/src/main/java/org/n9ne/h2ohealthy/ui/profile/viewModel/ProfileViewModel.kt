package org.n9ne.h2ohealthy.ui.profile.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.Setting
import org.n9ne.h2ohealthy.data.model.SettingItem
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class ProfileViewModel : ViewModel() {

    lateinit var navigator: Navigator

    val user = User("1", "Amir Hossein", "65", "180", "2002/11/30", 219)
    val settings = listOf(
        Setting("Password", R.drawable.ic_password, SettingItem.PASSWORD),
        Setting("Activity History", R.drawable.ic_history, SettingItem.HISTORY),
        Setting("Workout Progress", R.drawable.ic_progress, SettingItem.PROGRESS),
        Setting("Glasses", R.drawable.ic_password, SettingItem.GLASS),
    )

    fun editClick(@Suppress("UNUSED_PARAMETER") v: View) {
        navigator.shouldNavigate(R.id.profile_to_editProfile)
    }

    fun contactUsClick(@Suppress("UNUSED_PARAMETER") v: View) {
        //TODO validation
    }
}