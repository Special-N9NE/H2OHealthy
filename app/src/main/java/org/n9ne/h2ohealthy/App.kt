package org.n9ne.h2ohealthy

import android.app.Application
import com.google.firebase.FirebaseApp
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import org.n9ne.common.source.network.Client
import org.n9ne.common.util.Saver
import org.n9ne.common.util.Utils

@HiltAndroidApp
class App : Application() {

    lateinit var client: Client

    override fun onCreate() {
        super.onCreate()

        client = Client.getInstance()

        Saver.setup(this)

        FirebaseApp.initializeApp(this)

        val config =
            AppMetricaConfig.newConfigBuilder(Key.apiKey).build()
        AppMetrica.activate(this, config)


        Lingver.init(this, Utils.getLocal().language)
    }
}