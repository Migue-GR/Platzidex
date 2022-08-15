package com.pokedexplatzi.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pokedexplatzi.R
import com.pokedexplatzi.databinding.ActivityMainBinding
import com.pokedexplatzi.utils.AppSession
import com.pokedexplatzi.utils.AppSession.theAppNeedsToUpdateTheListOfPokemon
import com.pokedexplatzi.utils.ext.launchWithDelay
import com.pokedexplatzi.utils.ext.showToast

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHost: NavHostFragment
    private lateinit var navController: NavController
    private var doubleBackToExitPressedOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHost.navController
        observeUiEvents()
    }

    private fun observeUiEvents() {
        AppSession.run {
            globalProgressBar.observe(this@MainActivity) {
                binding.pgbMain.visibility = if (it) View.VISIBLE else View.GONE
                binding.bgProgressBar.visibility = if (it) View.VISIBLE else View.GONE
                binding.blockerViewsScreen.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.label) {
            PokeListFragment::class.simpleName -> pressAgainToExit()
            else -> super.onBackPressed()
        }
    }

    private fun pressAgainToExit() {
        if (doubleBackToExitPressedOnce) {
            theAppNeedsToUpdateTheListOfPokemon = true
            finishAffinity()
            return
        } else {
            doubleBackToExitPressedOnce = true
            showToast(getString(R.string.press_again_to_exit))
            lifecycleScope.launchWithDelay(TWO_SECONDS) { doubleBackToExitPressedOnce = false }
        }
    }

    companion object {
        private const val TWO_SECONDS = 2000L
    }
}