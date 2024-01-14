package org.n9ne.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import org.n9ne.auth.ui.viewModel.CompleteProfileViewModel
import org.n9ne.auth.ui.viewModel.LoginViewModel
import org.n9ne.auth.ui.viewModel.RegisterViewModel
import org.n9ne.auth.ui.viewModel.SplashViewModel

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideCompleteProfileViewModel(): CompleteProfileViewModel {
        return CompleteProfileViewModel()
    }

    @ViewModelScoped
    @Provides
    fun provideLoginViewModel(): LoginViewModel {
        return LoginViewModel()
    }

    @ViewModelScoped
    @Provides
    fun provideRegisterViewModel(): RegisterViewModel {
        return RegisterViewModel()
    }

    @ViewModelScoped
    @Provides
    fun provideSplashViewModel(): SplashViewModel {
        return SplashViewModel()
    }
}