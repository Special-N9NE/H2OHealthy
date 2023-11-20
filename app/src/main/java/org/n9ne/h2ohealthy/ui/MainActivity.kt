package org.n9ne.h2ohealthy.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.repo.MainRepo
import org.n9ne.h2ohealthy.data.repo.MainRepoLocalImpl
import org.n9ne.h2ohealthy.data.repo.local.AppDatabase
import org.n9ne.h2ohealthy.databinding.ActivityMainBinding
import org.n9ne.h2ohealthy.ui.dialog.addWaterDialog
import org.n9ne.h2ohealthy.ui.dialog.cupDialog
import org.n9ne.h2ohealthy.ui.home.adpter.CupsAdapter
import org.n9ne.h2ohealthy.util.Mapper.toLiter
import org.n9ne.h2ohealthy.util.Utils.isOnline
import org.n9ne.h2ohealthy.util.interfaces.AddWaterListener
import org.n9ne.h2ohealthy.util.interfaces.CupClickListener


class MainActivity : AppCompatActivity() {

    private lateinit var localRepo: MainRepo

    private var cups = listOf<Cup>()
    private lateinit var b: ActivityMainBinding
    private lateinit var cupAdapter: CupsAdapter
    private var selectedCup: Cup? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        init()
        setObserver()

        makeRequest {
            viewModel.getCups()
        }

        b.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    navController.popBackStack()
                    navController.navigate(R.id.home)
                }

                R.id.add -> {
                    navigationAddClick()
                }

                R.id.profile -> {
                    navController.popBackStack()
                    navController.navigate(R.id.profile)
                }

                else -> {}
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun init() {
        localRepo = MainRepoLocalImpl(AppDatabase.getDatabase(this).roomDao())
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun makeRequest(request: () -> Unit) {
        val repo = if (isOnline()) {
            //TODO pass apiRepo
            localRepo
        } else {
            localRepo
        }
        viewModel.repo = repo
        request.invoke()
    }

    fun goHome() {
        navController.popBackStack()
        navController.navigate(R.id.home)
    }

    fun reloadCups(list: List<Cup>) {
        cups = list
    }

    private fun setObserver() {
        viewModel.ldInsertWater.observe(this) {
            goHome()
        }
        viewModel.ldError.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
        viewModel.ldCups.observe(this) {
            cups = it
        }
    }

    private fun navigationAddClick() {
        makeRequest {
            viewModel.getCups()
        }
        val doneListener = object : AddWaterListener {
            override fun onAdd(amount: String) {
                //TODO change id
                val liter = amount.toDouble().toLiter()
                makeRequest {
                    viewModel.insertWater(liter.toString(), 1L)
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