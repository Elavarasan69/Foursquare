package com.robosoft.foursquare.view.activity

import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.robosoft.foursquare.R
import com.robosoft.foursquare.adapter.NearByCityAdapter
import com.robosoft.foursquare.adapter.SearchPlaceAdapter
import com.robosoft.foursquare.databinding.ActivitySearchBinding
import com.robosoft.foursquare.model.dataclass.hotel.HotelBody
import com.robosoft.foursquare.model.dataclass.search.SearchPlaceBody
import com.robosoft.foursquare.model.dataclass.search.SearchPlaceResponseBody
import com.robosoft.foursquare.viewModel.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var searchBinding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchPlaceResponseBody: SearchPlaceResponseBody
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        searchBinding.backIbn.setOnClickListener {
            onBackPressed()
        }

        searchBinding.filterIbn.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }

        val sharedPreferences =
            this.getSharedPreferences(
                "sharedPreference",
                Context.MODE_PRIVATE
            )
        val currentLat = sharedPreferences.getString("currentLat", "")!!
        val currentLong = sharedPreferences.getString("currentLong", "")!!

        val data = HotelBody(currentLat, currentLong)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        nearByCity()
        searchFromFavourite(data)

        if(searchBinding.search.isEmpty()){
            searchBinding.mapSearch.visibility = View.GONE
            searchBinding.listSubmitView.visibility = View.GONE
            searchBinding.searchPlace.visibility = View.GONE
            searchBinding.mapSubmitView.visibility = View.GONE
            searchBinding.searchHome.visibility = View.VISIBLE
        }

        searchBinding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getPlace()
                val searchData = SearchPlaceBody(currentLat, currentLong, query!!)
                getPlaceData(searchData)
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                getPlace()
                val searchData = SearchPlaceBody(currentLat, currentLong, text!!)
                getPlaceData(searchData)
                return false
            }
        })



        searchBinding.mapSubmitView.setOnClickListener {

            searchBinding.mapSubmitView.visibility = View.GONE
            searchBinding.searchHome.visibility = View.GONE
            searchBinding.searchPlace.visibility = View.GONE

            searchBinding.mapSearch.visibility = View.VISIBLE


            searchBinding.mapRecyclerView.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            searchBinding.mapRecyclerView.adapter =
                SearchPlaceAdapter(this, searchPlaceResponseBody, lifecycleScope)

            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.search_map_view) as SupportMapFragment
            mapFragment.getMapAsync{
                mMap = it

                for(i in searchPlaceResponseBody.result){
                    val coordinates = i.location.coordinates
                    val placeLatLong = LatLng(coordinates[1],coordinates[0])

                    mMap?.apply {
                        addMarker(MarkerOptions().position(placeLatLong).title(i.placeName))
                        moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLong,12f))
                    }
                }
            }
            searchBinding.listSubmitView.visibility = View.VISIBLE
        }
        searchBinding.listSubmitView.setOnClickListener {

            searchBinding.searchHome.visibility = View.GONE
            searchBinding.mapSearch.visibility = View.GONE
            searchBinding.listSubmitView.visibility = View.GONE
            searchBinding.searchPlace.visibility = View.VISIBLE
            searchBinding.mapSubmitView.visibility = View.VISIBLE
        }
    }


    fun nearByCity() {
        viewModel.getFavouriteLiveDataObserver().observe(this, Observer {
            if (it != null) {
                searchBinding.mapSubmitView.visibility = View.GONE
                searchBinding.searchPlace.visibility = View.GONE
                searchBinding.mapSearch.visibility = View.GONE
                searchBinding.listSubmitView.visibility = View.GONE
                searchBinding.searchHome.visibility = View.VISIBLE


                searchBinding.searchRecyclerView.layoutManager =
                    LinearLayoutManager(this)
                searchBinding.searchRecyclerView.adapter =
                    NearByCityAdapter(this, it, lifecycleScope, searchBinding.search)
            } else {
                Toast.makeText(
                    this,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun searchFromFavourite(data: HotelBody) {
        viewModel.getNearByCity(data)
    }

    fun getPlace() {
        viewModel.getSearchPlaceLiveDataObserver().observe(this, Observer {
            if (it != null) {

                searchBinding.searchHome.visibility = View.GONE
                searchBinding.mapSearch.visibility = View.GONE
                searchBinding.listSubmitView.visibility = View.GONE
                searchBinding.searchPlace.visibility = View.VISIBLE
                searchBinding.mapSubmitView.visibility = View.VISIBLE

                searchPlaceResponseBody = it

                searchBinding.placeRecyclerView.layoutManager =
                    LinearLayoutManager(this)
                searchBinding.placeRecyclerView.adapter =
                    SearchPlaceAdapter(this, it, lifecycleScope)
            } else {
                Toast.makeText(
                    this,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun getPlaceData(data: SearchPlaceBody) {
        viewModel.getSearchPlace(data)
    }
}