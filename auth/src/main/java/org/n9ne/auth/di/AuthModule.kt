package org.n9ne.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import org.n9ne.auth.repo.AuthRepo
import org.n9ne.auth.repo.AuthRepoImpl
import org.n9ne.common.di.LocalRepo
import org.n9ne.common.source.network.Client

@Module
@InstallIn(ActivityComponent::class)
class AuthModule {

    @ActivityScoped
    @Provides
    fun provideAuthRepoApi(): AuthRepo {
        return AuthRepoImpl(Client.getInstance())
    }

    @LocalRepo
    @ActivityScoped
    @Provides
    fun provideAuthRepoLocal(): AuthRepo {
        return AuthRepoImpl(Client.getInstance())
    }
}


