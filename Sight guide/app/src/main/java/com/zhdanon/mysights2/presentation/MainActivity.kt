package com.zhdanon.mysights2.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.AppBarConfiguration
import com.zhdanon.mysights2.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { mapPermissions ->
            if (mapPermissions.values.all { it }) {
                startMapFragment()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
    }

    // установка возможности возврата кнопкой ToolBar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun checkPermission() {
        if (REQUEST_PERMISSIONS.all { permission ->
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            startMapFragment()
        } else {
            launcher.launch(REQUEST_PERMISSIONS)
        }
    }

    private fun startMapFragment() {
        setContentView(R.layout.activity_main)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_container)

        if (currentFragment == null) {
            val fragment = GMapFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_container, fragment)
                .commit()
        }

        // установка изменения в ToolBar label фрагмента
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    companion object {
        private val REQUEST_PERMISSIONS: Array<String> = buildList {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }.toTypedArray()
    }
}