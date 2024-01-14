package org.n9ne.profile.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import org.n9ne.profile.ui.viewModel.AvatarsViewModel
import org.n9ne.profile.ui.viewModel.CupsViewModel
import org.n9ne.profile.ui.viewModel.EditProfileViewModel
import org.n9ne.profile.ui.viewModel.LeagueViewModel
import org.n9ne.profile.ui.viewModel.ProfileViewModel
import org.n9ne.profile.ui.viewModel.StatsViewModel

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideAvatarsViewModel(): AvatarsViewModel {
        return AvatarsViewModel()
    }

    @ViewModelScoped
    @Provides
    fun provideCupsViewModel(): CupsViewModel {
        return CupsViewModel()
    }

    @ViewModelScoped
    @Provides
    fun provideEditProfileViewModel(): EditProfileViewModel {
        return EditProfileViewModel()
    }

    @ViewModelScoped
    @Provides
    fun provideLeagueViewModel(): LeagueViewModel {
        return LeagueViewModel()
    }

    @ViewModelScoped
    @Provides
    fun provideProfileViewModel(): ProfileViewModel {
        return ProfileViewModel()
    }

    @ViewModelScoped
    @Provides
    fun provideStatsViewModel(): StatsViewModel {
        return StatsViewModel()
    }
}