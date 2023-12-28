package org.n9ne.h2ohealthy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.simform.refresh.SSPullToRefreshLayout
import org.n9ne.common.BaseActivity
import org.n9ne.common.util.interfaces.RefreshListener
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.databinding.ActivityAuthBinding


class AuthActivity : BaseActivity() {

    private lateinit var b: ActivityAuthBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(b.root)

        init()

        b.ssPullRefresh.setRefreshing(true)

        b.ssPullRefresh.setOnRefreshListener {
            val navHostFragment: Fragment? =
                supportFragmentManager.findFragmentById(R.id.navHost)
            val currentFragment: Fragment? =
                navHostFragment?.childFragmentManager?.fragments?.get(0)


            if (currentFragment is RefreshListener) {
                val listener = currentFragment as RefreshListener
                listener.onRefresh()
            } else {
                stopLoading()
            }
        }
    }

    private fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController

        b.ssPullRefresh.setRepeatMode(SSPullToRefreshLayout.RepeatMode.REPEAT)
        b.ssPullRefresh.setRepeatCount(SSPullToRefreshLayout.RepeatCount.INFINITE)
        b.ssPullRefresh.setLottieAnimation("loading.json")

        initLoading(b.ssPullRefresh)
    }
}