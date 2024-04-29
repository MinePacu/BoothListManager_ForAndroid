package com.minepacu.boothlistmanager

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.OnBackStackChangedListener
import androidx.fragment.app.FragmentManager.findFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.minepacu.boothlistmanager.databinding.ActivityMainBinding
import com.minepacu.boothlistmanager.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var context: Context

    public lateinit var toolbar: androidx.appcompat.widget.Toolbar

    private lateinit var mDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*
        context = this

        val toolBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.app_Bar)
        toolbar = toolBar
        //mDrawerToggle = ActionBarDrawerToggle(this, binding.drawerlayout, toolBar, R.string.open_Drawer, R.string.close_Drawer)
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolBar.setNavigationOnClickListener {
            onBackPressed()
        }

        */
        setSupportActionBar(findViewById(R.id.app_Bar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.show()
        //supportActionBar?.subtitle = "Test"
        //supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_addbooth, R.id.nav_serach, R.id.nav_hyperlinkgenerator, R.id.nav_preference), binding.drawerlayout
            )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
             android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    class BackStackListener(activity: MainActivity): FragmentManager.OnBackStackChangedListener {
        lateinit var mainActivity:MainActivity
        init {
            mainActivity = activity
        }
        override fun onBackStackChanged() {
            if (mainActivity.supportFragmentManager.backStackEntryCount > 0) {
                mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
                mainActivity.mDrawerToggle
                mainActivity.toolbar.setNavigationOnClickListener {
                    it.setOnClickListener {
                        mainActivity.onBackPressed()
                    }
                }
            } else {
                mainActivity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            }
        }
    }
}