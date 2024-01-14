package org.n9ne.common.di

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.n9ne.common.BaseActivity
import org.n9ne.common.source.local.AppDatabase

@Module
@InstallIn(ActivityComponent::class)
object AppModule {
    @Provides
    fun provideBaseActivity(activity: Activity): BaseActivity {
        check(activity is BaseActivity) { "Every Activity is expected to extend BaseActivity" }
        return activity
    }

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
}