package com.example.visioglobe

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.visioglobe.databinding.ActivityMainBinding
import com.example.visioglobe.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel.isAdmin.observe(this, androidx.lifecycle.Observer {
            setupAdmin(it)
        })

        setupNavigation()
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.bottomNavBar
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(navView, navController)
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.confirmRegisterFragment,
                R.id.confirmResetPasswordFragment,
                R.id.incidentDeclarationFragment,
                R.id.loginFragment,
                R.id.resetPasswordFragment,
                R.id.registerFragment -> navView.visibility = View.GONE
                R.id.homeFragment -> {
                    viewModel.userIsConnected()
                    if (viewModel.isConnected.value) {
                        navView.visibility = View.VISIBLE
                    } else {
                        navView.visibility = View.GONE
                    }
                }
                else -> navView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupAdmin(isAdmin: Boolean) {
        val navView: BottomNavigationView = binding.bottomNavBar
        navView.menu.findItem(R.id.adminFragment).isVisible = isAdmin
    }

    companion object {
        private const val TAG = "CurrentUser"
    }
}