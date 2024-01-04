package org.n9ne.h2ohealthy

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import org.n9ne.common.source.network.Client
import org.n9ne.common.util.Saver
import org.n9ne.common.util.Utils


class App : Application() {

    lateinit var client: Client

    override fun onCreate() {
        super.onCreate()

        client = Client.getInstance()

        Saver.setup(this)
    }

    override fun attachBaseContext(newBase: Context) {
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(Utils.getLocal())
        newBase.createConfigurationContext(config)
        super.attachBaseContext(newBase)
    }

}