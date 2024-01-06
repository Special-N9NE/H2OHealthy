package org.n9ne.common

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.simform.refresh.SSPullToRefreshLayout
import com.yariksoffice.lingver.Lingver
import org.n9ne.common.model.Cup
import org.n9ne.common.util.Utils
import org.n9ne.common.util.customViews.BottomNavigationViewWithIndicator

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

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocaleService.updateBaseContextLocale(newBase)
        )
    }

    protected fun setLocal() {

        val myLocale = Utils.getLocal()
        Lingver.getInstance().setLocale(this, myLocale.language)
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