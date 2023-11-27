package org.n9ne.h2ohealthy

import android.app.Application
import org.n9ne.h2ohealthy.data.source.network.Client

class App : Application() {

    lateinit var client: Client

    override fun onCreate() {
        super.onCreate()

        client = Client.getInstance()
    }
}