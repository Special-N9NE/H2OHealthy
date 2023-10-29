package org.n9ne.h2ohealthy.ui.login.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class LoginDoneViewModel : ViewModel() {
    lateinit var navigator: Navigator

    fun homeClick(@Suppress("UNUSED_PARAMETER") v: View) {
        navigator.shouldNavigate(R.id.login)
    }
}