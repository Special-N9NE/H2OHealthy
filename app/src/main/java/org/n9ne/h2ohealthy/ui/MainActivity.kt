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
import org.n9ne.h2ohealthy.data.repo.HomeRepo
import org.n9ne.h2ohealthy.data.repo.local.AppDatabase
import org.n9ne.h2ohealthy.databinding.ActivityMainBinding
import org.n9ne.h2ohealthy.ui.home.adpter.CupsAdapter
import org.n9ne.h2ohealthy.util.interfaces.AddWaterListener
import org.n9ne.h2ohealthy.util.interfaces.CupClickListener
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
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
        val repo = HomeRepo(AppDatabase.getDatabase(this).roomDao())
        viewModel = ViewModelProvider(this, MainViewModelFactory(repo))[MainViewModel::class.java]
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setObserver() {
        viewModel.ldInsertWater.observe(this) {
            navController.popBackStack()
            navController.navigate(R.id.home)
        }
        viewModel.ldError.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun navigationAddClick() {
        //TODO get cups
        val doneListener = object : AddWaterListener {
            override fun onAdd(amount: String) {
                //TODO change id
                val liter = amount.toDouble() / 1000
                viewModel.insertWater(liter.toString(), 1L)
            }
        }
        val cupDialog = this.cupDialog(layoutInflater)
        var dialog = this.addDialog(layoutInflater, null, doneListener) {
            cupDialog.show()
        }

        dialog.show()

        cupAdapter = CupsAdapter(viewModel.cups, object : CupClickListener {
            override fun onClick(item: Cup) {
                selectedCup = item
                cupDialog.dismiss()

                dialog =
                    this@MainActivity.addDialog(layoutInflater, item, doneListener) {
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