package com.ort.altoqueperro.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ort.altoqueperro.R
import com.ort.altoqueperro.databinding.ActivityHomeNavigationBinding

class HomeNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityHomeNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.background = null
        navView.menu.getItem(2).isEnabled = false

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView4) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.newMapModeFragment,
                R.id.newProfileUserFragment,
                R.id.settings,
                R.id.listMode
            )
        )


        val floatingButtonAction: View = findViewById(R.id.btnFloatingActions)
        val lostPetBtn: View = findViewById(R.id.fab1)
        val foundPetBtn: View = findViewById(R.id.fab2)

        //ToDo fijarse si conviene hacerlo al revés
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.petFound -> setAppBarInvisible()
                R.id.petFound2 -> setAppBarInvisible()
                R.id.petFound3 -> setAppBarInvisible()
                R.id.petFoundConfirmation -> setAppBarInvisible()
                R.id.petLost -> setAppBarInvisible()
                R.id.petLost2 -> setAppBarInvisible()
                R.id.petLost3 -> setAppBarInvisible()
                R.id.petLostConfirmation -> setAppBarInvisible()
                R.id.petLostSearchSimilarities -> setAppBarInvisible()
                R.id.similarPetFragment -> setAppBarInvisible()
                R.id.petFoundHostQuestion -> setAppBarInvisible()
                R.id.petFoundHostNo -> setAppBarInvisible()
                R.id.petFoundHostYes -> setAppBarInvisible()
                R.id.petFoundHostQuestion -> setAppBarInvisible()
                R.id.petLostFinalMessage -> setAppBarInvisible()
                else -> setAppBarVisible()
            }
        }
        floatingButtonAction.setOnClickListener {
            if (lostPetBtn.isInvisible) {
                lostPetBtn.isInvisible = false
                foundPetBtn.isInvisible = false
            } else {
                lostPetBtn.isInvisible = true
                foundPetBtn.isInvisible = true
            }
        }

        lostPetBtn.setOnClickListener {
            lostPetBtn.isInvisible = true
            foundPetBtn.isInvisible = true
            navController.navigate(R.id.petLost)
        }

        foundPetBtn.setOnClickListener {
            lostPetBtn.isInvisible = true
            foundPetBtn.isInvisible = true
            navController.navigate(R.id.petFound)
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setAppBarInvisible() {
        val floatingButtonAction: View = findViewById(R.id.btnFloatingActions)
        val navBar: BottomAppBar = this.findViewById(R.id.bottomAppBar)

        floatingButtonAction.visibility = View.GONE
        navBar.visibility = View.GONE
    }

    private fun setAppBarVisible() {
        val floatingButtonAction: View = findViewById(R.id.btnFloatingActions)
        val navBar: BottomAppBar = this.findViewById(R.id.bottomAppBar)

        floatingButtonAction.visibility = View.VISIBLE
        navBar.visibility = View.VISIBLE
    }
}