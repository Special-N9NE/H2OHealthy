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
import org.n9ne.h2ohealthy.databinding.ActivityMainBinding
import org.n9ne.h2ohealthy.ui.home.adpter.CupsAdapter
import org.n9ne.h2ohealthy.util.interfaces.CupClickListener


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
                    //TODO go to profile
                    navController.popBackStack()
                    //navController.navigate()
                }

                else -> {}
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun navigationAddClick() {
        //TODO add data
        //TODO get cups
        val doneListener = object : CupClickListener {
            override fun onClick(item: Cup) {
                //TODO add to database
                Toast.makeText(applicationContext, "Done", Toast.LENGTH_LONG)
                    .show()
            }
        }
        val cupDialog = this.cupDialog(layoutInflater)
        var dialog = this.addDialog(layoutInflater) {
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