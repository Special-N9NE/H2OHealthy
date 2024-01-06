package org.n9ne.common

import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.simform.refresh.SSPullToRefreshLayout
import org.n9ne.common.model.Cup
import org.n9ne.common.util.Utils
import org.n9ne.common.util.customViews.BottomNavigationViewWithIndicator
import java.util.Locale

abstract class BaseActivity : AppCompatActivity() {

    protected var cups = listOf<Cup>()

    private lateinit var ssPullRefresh: SSPullToRefreshLayout
    private var nav: BottomNavigationViewWithIndicator? = null

    fun reloadLanguage() {
        setLocal()

        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)

    }

    protected fun setLocal() {
        val myLocale = Utils.getLocal()
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.setLocale(myLocale)
        Locale.setDefault(myLocale)
        conf.setLayoutDirection(myLocale)
        res.updateConfiguration(conf, dm)
    }

    fun initLoading(view: SSPullToRefreshLayout) {
        ssPullRefresh = view
    }

    fun startLoading() {
        ssPullRefresh.setRefreshing(true)
    }

    fun stopLoading() {
        ssPullRefresh.setRefreshing(false)
    }


    fun initNav(view: BottomNavigationViewWithIndicator) {
        nav = view
    }

    fun showNavigation() {
        nav?.let {
            it.visibility = View.VISIBLE
        }
    }

    fun hideNavigation() {
        nav?.let {
            it.visibility = View.GONE
        }
    }

    fun reloadCups(list: List<Cup>) {
        cups = list
    }

}