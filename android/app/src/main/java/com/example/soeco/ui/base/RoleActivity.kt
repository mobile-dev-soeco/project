package com.example.soeco.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.soeco.R
import com.example.soeco.ui.auth.AuthActivity
import com.example.soeco.utils.viewModelFactory
import com.google.android.material.navigation.NavigationView

open class RoleActivity(
    private var layoutId: Int,
    private var navHostId: Int,
    private var navViewId: Int,
    private var drawLayoutId: Int,
): AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val roleActivityViewModel by viewModels<RoleActivityViewModel> { viewModelFactory }
    private lateinit var draw: DrawerLayout
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutId)

        draw = findViewById(drawLayoutId)

        val navHostFragment = supportFragmentManager.findFragmentById(navHostId) as NavHostFragment
        navController = navHostFragment.navController

        val navView: NavigationView = findViewById(navViewId)

        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)

        roleActivityViewModel.isAuthorized.observe(this, ::handleAuthorizationChange)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
            roleActivityViewModel.logout()
            return false
        }
        navController.navigate(item.itemId)
        draw.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleAuthorizationChange(isAuthorized: Boolean) {
        // If isAuthorized is false. Redirect to AuthActivity
        if (!isAuthorized){
            startActivity(Intent(application, AuthActivity::class.java))
            finish()
        }
    }
}