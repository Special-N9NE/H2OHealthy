package org.n9ne.h2ohealthy.ui.login.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.data.source.local.UserEntity
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class SplashViewModel : ViewModel() {
    lateinit var navigator: Navigator

    fun startClick() {
        navigator.shouldNavigate(R.id.splash_to_register)
    }


}