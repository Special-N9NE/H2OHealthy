package org.n9ne.h2ohealthy.ui.login.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class SplashViewModel : ViewModel() {
    lateinit var navigator: Navigator

    fun startClick(@Suppress("UNUSED_PARAMETER") v: View) {
        navigator.shouldNavigate(R.id.splash_to_register)
    }
}