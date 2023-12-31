package org.n9ne.h2ohealthy

import android.app.Application
import org.n9ne.common.source.network.Client

class App : Application() {

    lateinit var client: Client

    override fun onCreate() {
        super.onCreate()

        client = Client.getInstance()
    }
//    override fun attachBaseContext(base: Context) {
//        val config = Configuration()
//        val locale = Locale("en")
//        Locale.setDefault(locale)
//        config.setLocale(locale)
//        val newContext = base.createConfigurationContext(config)
//        super.attachBaseContext(newContext)
//    }
}