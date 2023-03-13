package com.robosoft.foursquare.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.robosoft.foursquare.R
import com.robosoft.foursquare.SharedPreferenceManager
import com.robosoft.foursquare.adapter.FilterAdapter
import com.robosoft.foursquare.databinding.ActivityFilterInFavouriteBinding
import com.robosoft.foursquare.model.dataclass.filter.FilterBody
import com.robosoft.foursquare.viewModel.FilterViewModel

class FilterInFavouriteActivity : AppCompatActivity() {

    private lateinit var filterINFavouriteBinding: ActivityFilterInFavouriteBinding
    private lateinit var viewModel: FilterViewModel

    lateinit var sortBy: String
    var priceRange: Int = 0
    var text = ""
    var acceptCard = false
    var delivery = false
    var dogFriendly = false
    var familyFriendlyPlace = false
    var inWalkingDistance = false
    var outdoorSeating = false
    var parking = false
    var wifi = false

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val sharedPreferences =
            this.getSharedPreferences(
                "sharedPreference",
                Context.MODE_PRIVATE
            )
        val accessToken = SharedPreferenceManager(this).getAccessToken()
        val currentLat = sharedPreferences.getString("currentLat", "")!!
        val currentLong = sharedPreferences.getString("currentLong", "")!!

        filterINFavouriteBinding = DataBindingUtil.setContentView(this, R.layout.activity_filter_in_favourite)

        viewModel = ViewModelProvider(this)[FilterViewModel::class.java]


        filterINFavouriteBinding.backIbn.setOnClickListener {
            onBackPressed()
        }

        var popular = false
        var distance = false
        var rating = false

        filterINFavouriteBinding.popular.setOnClickListener {
            if (!popular) {
                filterINFavouriteBinding.popular.setImageResource(R.drawable.popular_selected)
                popular = true
                filterINFavouriteBinding.distance.setImageResource(R.drawable.distance)
                distance = false
                filterINFavouriteBinding.rating.setImageResource(R.drawable.rating)
                rating = false
            } else {
                filterINFavouriteBinding.popular.setImageResource(R.drawable.popular)
                popular = false
            }
        }

        filterINFavouriteBinding.distance.setOnClickListener {
            if (!distance) {
                filterINFavouriteBinding.distance.setImageResource(R.drawable.distance_selected)
                distance = true
                filterINFavouriteBinding.popular.setImageResource(R.drawable.popular)
                popular = false
                filterINFavouriteBinding.rating.setImageResource(R.drawable.rating)
                rating = false
            } else {
                filterINFavouriteBinding.distance.setImageResource(R.drawable.distance)
                distance = false
            }
        }

        filterINFavouriteBinding.rating.setOnClickListener {
            if (!rating) {
                filterINFavouriteBinding.rating.setImageResource(R.drawable.rating_selected)
                rating = true
                filterINFavouriteBinding.distance.setImageResource(R.drawable.distance)
                distance = false
                filterINFavouriteBinding.popular.setImageResource(R.drawable.popular)
                popular = false
            } else {
                filterINFavouriteBinding.rating.setImageResource(R.drawable.rating)
                rating = false
            }
        }

        var rupeeOne = false
        var rupeeTwo = false
        var rupeeThree = false
        var rupeeFour = false


        filterINFavouriteBinding.rupee1.setOnClickListener {
            if (!rupeeOne) {
                filterINFavouriteBinding.rupee1.setImageResource(R.drawable.ruppe_btn1_selected)
                filterINFavouriteBinding.rupee2.setImageResource(R.drawable.ruppe_btn2)
                filterINFavouriteBinding.rupee3.setImageResource(R.drawable.ruppe_btn3)
                filterINFavouriteBinding.rupee4.setImageResource(R.drawable.ruppe_btn4)
                rupeeOne = true
                rupeeTwo = false
                rupeeThree = false
                rupeeFour = false
            } else {
                filterINFavouriteBinding.rupee1.setImageResource(R.drawable.ruppe_btn1)
                rupeeOne = false
            }
        }

        filterINFavouriteBinding.rupee2.setOnClickListener {
            if (!rupeeTwo) {
                filterINFavouriteBinding.rupee2.setImageResource(R.drawable.ruppe_btn2_selected)
                filterINFavouriteBinding.rupee1.setImageResource(R.drawable.ruppe_btn1)
                filterINFavouriteBinding.rupee3.setImageResource(R.drawable.ruppe_btn3)
                filterINFavouriteBinding.rupee4.setImageResource(R.drawable.ruppe_btn4)
                rupeeTwo = true
                rupeeOne = false
                rupeeThree = false
                rupeeFour = false

            } else {
                filterINFavouriteBinding.rupee2.setImageResource(R.drawable.ruppe_btn2)
                rupeeTwo = false
            }
        }

        filterINFavouriteBinding.rupee3.setOnClickListener {
            if (!rupeeThree) {
                filterINFavouriteBinding.rupee3.setImageResource(R.drawable.ruppe_btn3_selected)
                filterINFavouriteBinding.rupee1.setImageResource(R.drawable.ruppe_btn1)
                filterINFavouriteBinding.rupee2.setImageResource(R.drawable.ruppe_btn2)
                filterINFavouriteBinding.rupee4.setImageResource(R.drawable.ruppe_btn4)
                rupeeThree = true
                rupeeOne = false
                rupeeTwo = false
                rupeeFour = false
            } else {
                filterINFavouriteBinding.rupee3.setImageResource(R.drawable.ruppe_btn3)
                rupeeThree = false
            }
        }

        filterINFavouriteBinding.rupee4.setOnClickListener {
            if (!rupeeFour) {
                filterINFavouriteBinding.rupee4.setImageResource(R.drawable.ruppe_btn4_selected)
                filterINFavouriteBinding.rupee1.setImageResource(R.drawable.ruppe_btn1)
                filterINFavouriteBinding.rupee2.setImageResource(R.drawable.ruppe_btn2)
                filterINFavouriteBinding.rupee3.setImageResource(R.drawable.ruppe_btn3)
                rupeeFour = true
                rupeeOne = false
                rupeeTwo = false
                rupeeThree = false

            } else {
                filterINFavouriteBinding.rupee4.setImageResource(R.drawable.ruppe_btn4)
                rupeeFour = false
            }
        }



        filterINFavouriteBinding.acceptCard.setOnClickListener {
            if (!acceptCard) {
                filterINFavouriteBinding.acceptCardTv.setTextColor(R.color.selectedColor)
                filterINFavouriteBinding.acceptCardIv.setImageResource(R.drawable.filter_selected)
                acceptCard = true
            } else {
                filterINFavouriteBinding.acceptCardTv.setTextColor(Color.parseColor("#8D8D8D"))
                filterINFavouriteBinding.acceptCardIv.setImageResource(R.drawable.icon_add)
                acceptCard = false
            }
        }

        filterINFavouriteBinding.delivery.setOnClickListener {
            if (!delivery) {
                filterINFavouriteBinding.deliveryTv.setTextColor(R.color.selectedColor)
                filterINFavouriteBinding.deliveryIv.setImageResource(R.drawable.filter_selected)
                delivery = true
            } else {
                filterINFavouriteBinding.deliveryTv.setTextColor(Color.parseColor("#8D8D8D"))
                filterINFavouriteBinding.deliveryIv.setImageResource(R.drawable.icon_add)
                delivery = false
            }
        }

        filterINFavouriteBinding.dogFriendly.setOnClickListener {
            if (!dogFriendly) {
                filterINFavouriteBinding.dogFriendlyTv.setTextColor(R.color.selectedColor)
                filterINFavouriteBinding.dogFriendlyIv.setImageResource(R.drawable.filter_selected)
                dogFriendly = true
            } else {
                filterINFavouriteBinding.dogFriendlyTv.setTextColor(Color.parseColor("#8D8D8D"))
                filterINFavouriteBinding.dogFriendlyIv.setImageResource(R.drawable.icon_add)
                dogFriendly = false
            }
        }

        filterINFavouriteBinding.familyFriendlyPlaces.setOnClickListener {
            if (!familyFriendlyPlace) {
                filterINFavouriteBinding.familyFriendlyPlacesTv.setTextColor(R.color.selectedColor)
                filterINFavouriteBinding.familyFriendlyPlacesIv.setImageResource(R.drawable.filter_selected)
                familyFriendlyPlace = true
            } else {
                filterINFavouriteBinding.familyFriendlyPlacesTv.setTextColor(Color.parseColor("#8D8D8D"))
                filterINFavouriteBinding.familyFriendlyPlacesIv.setImageResource(R.drawable.icon_add)
                familyFriendlyPlace = false
            }
        }

        filterINFavouriteBinding.inWalkDistance.setOnClickListener {
            if (!inWalkingDistance) {
                filterINFavouriteBinding.inWalkDistanceTv.setTextColor(R.color.selectedColor)
                filterINFavouriteBinding.inWalkDistanceIv.setImageResource(R.drawable.filter_selected)
                inWalkingDistance = true
            } else {
                filterINFavouriteBinding.inWalkDistanceTv.setTextColor(Color.parseColor("#8D8D8D"))
                filterINFavouriteBinding.inWalkDistanceIv.setImageResource(R.drawable.icon_add)
                inWalkingDistance = false
            }
        }

        filterINFavouriteBinding.outdoorSeating.setOnClickListener {
            if (!outdoorSeating) {
                filterINFavouriteBinding.outdoorSeatingTv.setTextColor(R.color.selectedColor)
                filterINFavouriteBinding.outdoorSeatingIv.setImageResource(R.drawable.filter_selected)
                outdoorSeating = true
            } else {
                filterINFavouriteBinding.outdoorSeatingTv.setTextColor(Color.parseColor("#8D8D8D"))
                filterINFavouriteBinding.outdoorSeatingIv.setImageResource(R.drawable.icon_add)
                outdoorSeating = false
            }
        }

        filterINFavouriteBinding.parking.setOnClickListener {
            if (!parking) {
                filterINFavouriteBinding.parkingTv.setTextColor(R.color.selectedColor)
                filterINFavouriteBinding.parkingIv.setImageResource(R.drawable.filter_selected)
                parking = true
            } else {
                filterINFavouriteBinding.parkingTv.setTextColor(Color.parseColor("#8D8D8D"))
                filterINFavouriteBinding.parkingIv.setImageResource(R.drawable.icon_add)
                parking = false
            }
        }

        filterINFavouriteBinding.wifi.setOnClickListener {
            if (!wifi) {
                filterINFavouriteBinding.wifiTv.setTextColor(R.color.selectedColor)
                filterINFavouriteBinding.wifiIv.setImageResource(R.drawable.filter_selected)
                wifi = true
            } else {
                filterINFavouriteBinding.wifiTv.setTextColor(Color.parseColor("#8D8D8D"))
                filterINFavouriteBinding.wifiIv.setImageResource(R.drawable.icon_add)
                wifi = false
            }
        }



        filterINFavouriteBinding.doneTv.setOnClickListener {

            sortBy = if (popular) "popular"
            else if (distance) "totaldistance"
            else if (rating) "totalrating"
            else ""

            priceRange = if (rupeeOne) 9
            else if (rupeeTwo) 99
            else if (rupeeThree) 999
            else if (rupeeFour) 9999
            else 0

            text = filterINFavouriteBinding.search.query.toString()
            val radius = filterINFavouriteBinding.radiusEt.text.toString()


            if (sortBy.isEmpty()) {
                Toast.makeText(this, "select sort value", Toast.LENGTH_SHORT).show()
            } else if (radius.isEmpty()) {
                Toast.makeText(this, "enter radius value", Toast.LENGTH_SHORT).show()
            } else if (priceRange == 0) {
                Toast.makeText(this, "select price value", Toast.LENGTH_SHORT).show()
            } else {
                val data = FilterBody(
                    acceptCard,
                    delivery,
                    dogFriendly,
                    familyFriendlyPlace,
                    inWalkingDistance,
                    currentLat,
                    currentLong,
                    outdoorSeating,
                    parking,
                    priceRange,
                    radius.toInt(),
                    sortBy,
                    text,
                    wifi
                )
                Log.d("data", data.toString())
                getFilter()
                filterInFavourites(accessToken, data)
            }
        }
    }

    private fun getFilter() {
        viewModel.getFavFilterLiveDataObserver().observe(this, Observer {
            if (it != null) {
                Log.d("response", it.toString())
                filterINFavouriteBinding.filterView.visibility = View.GONE
                filterINFavouriteBinding.filterRecyclerLayout.visibility = View.VISIBLE
                filterINFavouriteBinding.filterInFavRecyclerView.layoutManager =
                    LinearLayoutManager(this)
                filterINFavouriteBinding.filterInFavRecyclerView.adapter =
                    FilterAdapter(this, it, lifecycleScope)
            } else {
                filterINFavouriteBinding.filterView.visibility = View.VISIBLE
                Toast.makeText(
                    this,
                    "select values",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun filterInFavourites(accessToken: String,data: FilterBody) {
        viewModel.filterInFavourites(accessToken, data)
    }
}