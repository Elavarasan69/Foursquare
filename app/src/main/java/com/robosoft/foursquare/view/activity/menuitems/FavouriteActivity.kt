package com.robosoft.foursquare.view.activity.menuitems

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.robosoft.foursquare.R
import com.robosoft.foursquare.SharedPreferenceManager
import com.robosoft.foursquare.adapter.FavouriteAdapter
import com.robosoft.foursquare.databinding.ActivityFavouriteBinding
import com.robosoft.foursquare.model.dataclass.favourites.GetFavSearchBody
import com.robosoft.foursquare.model.dataclass.hotel.HotelBody
import com.robosoft.foursquare.view.activity.FilterInFavouriteActivity
import com.robosoft.foursquare.viewModel.FavouriteViewModel

class FavouriteActivity : AppCompatActivity() {

    private lateinit var favouriteBinding: ActivityFavouriteBinding
    private lateinit var viewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favouriteBinding = DataBindingUtil.setContentView(this,R.layout.activity_favourite)

        val sharedPreferences =
            this.getSharedPreferences(
                "sharedPreference",
                Context.MODE_PRIVATE
            )
        val accessToken = SharedPreferenceManager(this).getAccessToken()
        val currentLat = sharedPreferences.getString("currentLat", "")!!
        val currentLong = sharedPreferences.getString("currentLong", "")!!

        Log.d("lat",currentLat)

        viewModel = ViewModelProvider(this)[FavouriteViewModel::class.java]

        favouriteBinding.backIbn.setOnClickListener {
            onBackPressed()
        }

        favouriteBinding.filterIbn.setOnClickListener {
            startActivity(Intent(this, FilterInFavouriteActivity::class.java))
        }

        favouriteBinding.searchSv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel()
                val data = GetFavSearchBody(currentLat,currentLong,query!!)
                searchFromFavourite(accessToken,data)
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                searchViewModel()
                val data = GetFavSearchBody(currentLat,currentLong,text!!)
                searchFromFavourite(accessToken,data)
                return false
            }

        })

        val data = HotelBody(currentLat,currentLong)
        viewModel.getFavouriteLiveDataObserver().observe(this, Observer {
            if (it != null) {
                Log.d("Favourite",it.toString())
                favouriteBinding.favouritesRecyclerView.layoutManager =
                    LinearLayoutManager(this)
                favouriteBinding.favouritesRecyclerView.adapter =
                    FavouriteAdapter(this, it,lifecycleScope)
            } else {
                Toast.makeText(
                    this,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        viewModel.getFavourite(accessToken, data)
    }

    fun searchViewModel(){
        viewModel.getSearchFavouriteLiveDataObserver().observe(this, Observer {
            if (it != null) {
                favouriteBinding.favouritesRecyclerView.layoutManager =
                    LinearLayoutManager(this)
                favouriteBinding.favouritesRecyclerView.adapter =
                    FavouriteAdapter(this, it,lifecycleScope)
            } else {
                Toast.makeText(
                    this,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    fun searchFromFavourite(accessToken: String, data: GetFavSearchBody) {
        viewModel.searchFromFavourite(accessToken, data)
    }
}