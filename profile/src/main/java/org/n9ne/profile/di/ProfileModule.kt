package org.n9ne.profile.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import org.n9ne.common.di.LocalRepo
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.source.network.Client
import org.n9ne.profile.repo.ProfileRepo
import org.n9ne.profile.repo.ProfileRepoApiImpl
import org.n9ne.profile.repo.ProfileRepoLocalImpl

@Module
@InstallIn(ActivityComponent::class)
class ProfileModule {

    @ActivityScoped
    @Provides
    fun provideProfileRepoApi(): ProfileRepo {
        return ProfileRepoApiImpl(Client.getInstance())
    }

    @LocalRepo
    @ActivityScoped
    @Provides
    fun provideProfileRepLocal(@ApplicationContext context: Context): ProfileRepo {
        return ProfileRepoLocalImpl(AppDatabase.getDatabase(context).roomDao())
    }
}