package org.n9ne.common

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.simform.refresh.SSPullToRefreshLayout
import org.n9ne.common.model.Cup
import org.n9ne.common.util.customViews.BottomNavigationViewWithIndicator

abstract class BaseActivity : AppCompatActivity() {

    protected var cups = listOf<Cup>()

    private lateinit var ssPullRefresh: SSPullToRefreshLayout
    private var nav: BottomNavigationViewWithIndicator? = null

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