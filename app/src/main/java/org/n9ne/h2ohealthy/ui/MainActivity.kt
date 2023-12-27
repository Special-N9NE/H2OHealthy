package org.n9ne.h2ohealthy.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.simform.refresh.SSPullToRefreshLayout
import org.n9ne.common.model.Cup
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Mapper.toLiter
import org.n9ne.common.util.Saver.getToken
import org.n9ne.common.util.Utils.isOnline
import org.n9ne.common.util.interfaces.AddWaterListener
import org.n9ne.common.util.interfaces.CupClickListener
import org.n9ne.common.util.interfaces.RefreshListener
import org.n9ne.h2ohealthy.App
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.repo.MainRepo
import org.n9ne.h2ohealthy.data.repo.MainRepoApiImpl
import org.n9ne.h2ohealthy.data.repo.MainRepoLocalImpl
import org.n9ne.h2ohealthy.databinding.ActivityMainBinding
import org.n9ne.h2ohealthy.ui.dialog.addWaterDialog
import org.n9ne.h2ohealthy.ui.dialog.cupDialog
import org.n9ne.h2ohealthy.ui.home.adpter.CupsAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var localRepo: MainRepo
    private lateinit var apiRepo: MainRepo

    private var cups = listOf<Cup>()
    private lateinit var b: ActivityMainBinding
    private lateinit var cupAdapter: CupsAdapter
    private var selectedCup: Cup? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    val home = org.n9ne.common.R.id.home
    val add = org.n9ne.common.R.id.add
    val profile = org.n9ne.common.R.id.profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        init()
        setObserver()

        makeRequest {
            viewModel.getCups(getToken())
        }

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

        b.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                home -> {
                    navController.popBackStack()
                    navController.navigate(home)
                }

                add -> {
                    navigationAddClick()
                }

                profile -> {
                    navController.popBackStack()
                    navController.navigate(profile)
                }

                else -> {}
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun init() {
        apiRepo = MainRepoApiImpl((application as App).client)
        localRepo = MainRepoLocalImpl(AppDatabase.getDatabase(this).roomDao())
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.db = AppDatabase.getDatabase(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController

        b.ssPullRefresh.setRepeatMode(SSPullToRefreshLayout.RepeatMode.REPEAT)
        b.ssPullRefresh.setRepeatCount(SSPullToRefreshLayout.RepeatCount.INFINITE)
        b.ssPullRefresh.setLottieAnimation("loading.json")
    }

    fun startLoading() {
        b.ssPullRefresh.setRefreshing(true)
    }

    fun stopLoading() {
        b.ssPullRefresh.setRefreshing(false)
    }

    private fun makeRequest(request: () -> Unit) {
        val repo = if (isOnline()) {
            apiRepo
        } else {
            localRepo
        }
        viewModel.repo = repo
        request.invoke()
    }

    private fun makeApiRequest(request: () -> Unit) {
        viewModel.repo = apiRepo
        request.invoke()
    }

    fun goHome() {
        navController.popBackStack()
        navController.navigate(home)
    }

    fun reloadCups(list: List<Cup>) {
        cups = list
    }

    private fun setObserver() {
        viewModel.ldInsertWater.observe(this, EventObserver {
            goHome()
        })
        viewModel.ldError.observe(this, EventObserver {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
        viewModel.ldCups.observe(this) {
            cups = it
        }
    }

    private fun navigationAddClick() {
        makeRequest {
            viewModel.getCups(getToken())
        }
        val doneListener = object : AddWaterListener {
            override fun onAdd(amount: String) {
                 val liter = amount.toDouble().toLiter()
                makeApiRequest {
                    viewModel.insertWater(liter.toString(), getToken())
                }
            }
        }

        val cupDialog = this.cupDialog(layoutInflater)
        var dialog = this.addWaterDialog(layoutInflater, null, doneListener) {
            cupDialog.show()
        }

        dialog.show()

        cupAdapter = CupsAdapter(cups,
            object : CupClickListener {
                override fun onClick(item: Cup) {
                    selectedCup = item
                    cupDialog.dismiss()

                    dialog =
                        this@MainActivity.addWaterDialog(layoutInflater, item, doneListener) {
                            cupDialog.show()
                        }
                    dialog.show()
                }
            })
        val rvCup = cupDialog.findViewById<RecyclerView>(R.id.rvCup)
        rvCup.adapter = cupAdapter

    }

    fun showNavigation() {
        b.bottomNavigation.visibility = View.VISIBLE
    }

    fun hideNavigation() {
        b.bottomNavigation.visibility = View.GONE
    }
}