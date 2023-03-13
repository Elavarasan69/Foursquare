package com.robosoft.foursquare.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robosoft.foursquare.databinding.ActivityIndividualHotelContainerBinding
import com.robosoft.foursquare.view.fragment.individualhotel.HotelDetailFragment

class IndividualHotelContainerActivity : AppCompatActivity() {

    private lateinit var individualHotelContainerBinding: ActivityIndividualHotelContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        individualHotelContainerBinding =
            ActivityIndividualHotelContainerBinding.inflate(layoutInflater)
        setContentView(individualHotelContainerBinding.root)

        val intent = intent
        val placeId = intent.getStringExtra("placeId")
        val placeName = intent.getStringExtra("placeName")
        val distance = intent.getStringExtra("distance")
        val rating = intent.getStringExtra("rating")
        val favourite = intent.getStringExtra("rating")

        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putString("placeId", placeId)
            bundle.putString("placeName", placeName)
            bundle.putString("distance", distance)
            bundle.putString("rating",rating)
            val fragment = HotelDetailFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(
                individualHotelContainerBinding.hotelContainer.id,
                fragment
            ).commit()
        }
    }
}