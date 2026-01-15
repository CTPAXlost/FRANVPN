package com.franvpn.app.ui

import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.franvpn.app.R
import com.franvpn.app.databinding.ActivityMainBinding
import com.franvpn.app.ui.fragment.ConnectionFragment
import com.franvpn.app.ui.fragment.ServersFragment
import com.franvpn.app.ui.fragment.SettingsFragment
import com.franvpn.app.ui.fragment.StatisticsFragment
import com.franvpn.app.ui.fragment.SubscriptionFragment
import com.franvpn.app.vpn.FranVpnService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Main Activity for FRANVPN
 * Implements tab-based navigation for different sections
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val connectionViewModel: com.franvpn.app.ui.viewmodel.MainViewModel by viewModel()
    
    private lateinit var pagerAdapter: MainPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Force dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        setupNavigation()
        setupVpnPermission()
        
        Timber.d("MainActivity created")
    }

    private fun setupUI() {
        // Setup ViewPager2 with fragments
        viewPager = binding.viewPager
        pagerAdapter = MainPagerAdapter(this)
        viewPager.adapter = pagerAdapter
        
        // Setup TabLayout
        tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
            tab.icon = getTabIcon(position)
        }.attach()
        
        // Setup BottomNavigationView as alternative navigation
        bottomNav = binding.bottomNavigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_connection -> viewPager.currentItem = 0
                R.id.nav_servers -> viewPager.currentItem = 1
                R.id.nav_subscriptions -> viewPager.currentItem = 2
                R.id.nav_statistics -> viewPager.currentItem = 3
                R.id.nav_settings -> viewPager.currentItem = 4
            }
            true
        }
        
        // Sync ViewPager with BottomNav
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNav.menu.getItem(position).isChecked = true
            }
        })
    }

    private fun setupNavigation() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupVpnPermission() {
        lifecycleScope.launch {
            val vpnIntent = VpnService.prepare(this@MainActivity)
            if (vpnIntent != null) {
                Timber.d("VPN permission not granted, requesting...")
                startActivityForResult(vpnIntent, VPN_PERMISSION_REQUEST_CODE)
            } else {
                Timber.d("VPN permission already granted")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VPN_PERMISSION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Timber.d("VPN permission granted")
            } else {
                Timber.w("VPN permission denied")
            }
        }
    }

    private fun getTabTitle(position: Int): String {
        return when (position) {
            0 -> "Connection"
            1 -> "Servers"
            2 -> "Subscriptions"
            3 -> "Statistics"
            4 -> "Settings"
            else -> ""
        }
    }

    private fun getTabIcon(position: Int): android.graphics.drawable.Drawable? {
        return when (position) {
            0 -> ContextCompat.getDrawable(this, R.drawable.ic_vpn)
            1 -> ContextCompat.getDrawable(this, R.drawable.ic_servers)
            2 -> ContextCompat.getDrawable(this, R.drawable.ic_subscription)
            3 -> ContextCompat.getDrawable(this, R.drawable.ic_statistics)
            4 -> ContextCompat.getDrawable(this, R.drawable.ic_settings)
            else -> null
        }
    }

    /**
     * Adapter for ViewPager2
     */
    private inner class MainPagerAdapter(activity: MainActivity) : FragmentStateAdapter(activity) {
        
        override fun getItemCount() = 5
        
        override fun createFragment(position: Int) = when (position) {
            0 -> ConnectionFragment()
            1 -> ServersFragment()
            2 -> SubscriptionFragment()
            3 -> StatisticsFragment()
            4 -> SettingsFragment()
            else -> ConnectionFragment()
        }
    }

    companion object {
        private const val VPN_PERMISSION_REQUEST_CODE = 1000
    }
}
