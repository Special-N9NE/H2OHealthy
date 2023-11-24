package org.n9ne.h2ohealthy.ui.login.viewModel

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.repo.local.AppDatabase
import org.n9ne.h2ohealthy.data.repo.local.UserEntity
import org.n9ne.h2ohealthy.util.interfaces.Navigator

class SplashViewModel : ViewModel() {
    lateinit var navigator: Navigator

    fun startClick() {
        navigator.shouldNavigate(R.id.splash_to_register)
    }

    fun initDatabase(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val user = UserEntity(
                    1L, 0L,
                    "test@gmail.com", "1234",
                    "2023/11/5", "Test",
                    "2002/11/30", 65,
                    181, 1,
                    0, "3", ""
                )
                AppDatabase.getDatabase(context).roomDao().insertUser(user)
            }
        }
    }
}