package com.example.home.di

import android.content.Context
import com.example.home.repo.HomeRepo
import com.example.home.repo.HomeRepoApiImpl
import com.example.home.repo.HomeRepoLocalImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import org.n9ne.common.di.LocalRepo
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.source.network.Client

@Module
@InstallIn(ActivityComponent::class)
class HomeModule {

    @ActivityScoped
    @Provides
    fun provideHomeRepoApi(): HomeRepo {
        return HomeRepoApiImpl(Client.getInstance())
    }

    @LocalRepo
    @ActivityScoped
    @Provides
    fun provideHomeRepoLocal(@ApplicationContext context: Context): HomeRepo {
        return HomeRepoLocalImpl(AppDatabase.getDatabase(context).roomDao())
    }
}


