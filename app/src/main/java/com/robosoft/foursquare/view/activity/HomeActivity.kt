package com.robosoft.foursquare.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.robosoft.foursquare.R
import com.robosoft.foursquare.SharedPreferenceManager
import com.robosoft.foursquare.adapter.ViewPagerAdapter
import com.robosoft.foursquare.databinding.ActivityHomeBinding
import com.robosoft.foursquare.model.dataclass.LatLong
import com.robosoft.foursquare.model.network.ProjectService
import com.robosoft.foursquare.view.activity.menuitems.AboutusActivity
import com.robosoft.foursquare.view.activity.menuitems.FavouriteActivity
import com.robosoft.foursquare.view.activity.menuitems.FeedbackActivity
import kotlinx.android.synthetic.main.menu_header.view.*
import kotlinx.android.synthetic.main.review_tab.view.*

class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding: ActivityHomeBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var latLng: LatLong
    private val projectApi = ProjectService()

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        val sharedPreferences =
            this.getSharedPreferences(
                "sharedPreference",
                Context.MODE_PRIVATE
            )
        val accessToken = SharedPreferenceManager(this).getAccessToken()
        val login = sharedPreferences?.getString("Login", "")!!

        drawerLayout = homeBinding.drawerLayout
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()


        homeBinding.toolbar.menu.setOnClickListener {
            drawerLayout.open()
        }


        val navigationView: NavigationView = homeBinding.navigationView


        if(login == "Login"){
            navigationView.getHeaderView(0).profile_img.setOnClickListener {
                Toast.makeText(this, "user Profile", Toast.LENGTH_SHORT).show()
            }
        } else {
            navigationView.getHeaderView(0).profile_img.setOnClickListener {
                Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
            }
        }


        if(login == "Login"){
            projectApi.getName(accessToken){
                if (it != null){
                    Log.d("name response", it.toString())
                    navigationView.getHeaderView(0).username_home.text = it.fullName
                } else {
                    Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                }
            }
        }
        else {
            navigationView.getHeaderView(0).username_home.text = "Login"
            navigationView.getHeaderView(0).username_home.setTextColor(Color.parseColor("#8D8D8D"))
            navigationView.getHeaderView(0).setOnClickListener {
                finish()
            }
        }


        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_favourites -> {
                    if (login == "Login") {
                        startActivity(Intent(this, FavouriteActivity::class.java))
                    } else {
                        Toast.makeText(this, "Login to see favourite", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.nav_feedback -> {
                    if (login == "Login") {
                        startActivity(Intent(this, FeedbackActivity::class.java))
                    } else {
                        Toast.makeText(this, "Login to add feedback", Toast.LENGTH_SHORT).show()
                    }

                }
                R.id.nav_about_us -> {

                    startActivity(Intent(this, AboutusActivity::class.java))
                }
                R.id.nav_logout -> {
                    if (login == "Login") {
                        val sharedPreference =
                            this.getSharedPreferences(
                                "sharedPreference",
                                Context.MODE_PRIVATE
                            )
                        val editor = sharedPreference?.edit()
                        editor?.putString("Login","Logout")
                        editor?.apply()
                        this.finish()
                    } else {
                        Toast.makeText(this, "Login first", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            true
        }

        tabLayout = homeBinding.tabLayout
        viewPager2 = homeBinding.viewPager2
        viewPager2.isUserInputEnabled = false

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)

                latLng = LatLong(location.latitude, location.longitude)

                val sharedPreferences =
                    this.getSharedPreferences(
                        "sharedPreference",
                        Context.MODE_PRIVATE
                    )
                val editor = sharedPreferences?.edit()
                editor?.putString("currentLat", location.latitude.toString())
                editor?.putString("currentLong", location.longitude.toString())
                editor?.apply()
                Log.d("location", currentLatLong.toString())
                viewPager2.adapter = ViewPagerAdapter(this, currentLatLong)

                TabLayoutMediator(tabLayout, viewPager2) { tab, index ->
                    tab.text = when (index) {
                        0 -> "Near You"
                        1 -> "Toppick"
                        2 -> "Popular"
                        3 -> "Lunch"
                        4 -> "Coffee"
                        else -> throw Resources.NotFoundException("Position Not Found")
                    }
                }.attach()
                viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                    }
                })
            }
        }
        homeBinding.toolbar.searchIbn.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        homeBinding.toolbar.filterIbn.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

}