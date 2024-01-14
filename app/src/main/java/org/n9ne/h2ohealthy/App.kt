package org.n9ne.h2ohealthy

import android.app.Application
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
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

        Lingver.init(this, Utils.getLocal().language)
    }
}