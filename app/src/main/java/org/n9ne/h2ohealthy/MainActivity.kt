package org.n9ne.h2ohealthy

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.databinding.ActivityMainBinding
import org.n9ne.h2ohealthy.ui.addDialog
import org.n9ne.h2ohealthy.ui.cupDialog


class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        val navController = navHostFragment.navController
        b.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    navController.popBackStack()
                    navController.navigate(R.id.home)
                }

                R.id.add -> {
                    //TODO add data
                    //TODO get cups
                    val cups = listOf(
                        Cup("My cup", 100, R.color.linearBlueEnd),
                        Cup("bottle", 500, R.color.linearPurpleEnd),
                        Cup("My cup", 2000, R.color.linearBlueStart),
                        Cup("My cup", 300, R.color.linearPurpleStart),
                        Cup("title", 400, R.color.white),
                        Cup("My cup", 100, R.color.gray),
                    )
                    val cupDialog = this.cupDialog(layoutInflater, cups)

                    val dialog = this.addDialog(layoutInflater) {
                        cupDialog.show()
                    }
                    dialog.show()
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

    fun showNavigation() {
        b.bottomNavigation.visibility = View.VISIBLE
    }

    fun hideNavigation() {
        b.bottomNavigation.visibility = View.GONE
    }
}