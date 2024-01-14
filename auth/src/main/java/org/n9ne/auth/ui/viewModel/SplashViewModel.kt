package org.n9ne.auth.ui.viewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import org.n9ne.auth.repo.AuthRepo
import org.n9ne.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel<AuthRepo>()