package com.robosoft.foursquare.view.fragment.individualhotel

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.robosoft.foursquare.R
import com.robosoft.foursquare.SharedPreferenceManager
import com.robosoft.foursquare.databinding.FragmentHotelDetailBinding
import com.robosoft.foursquare.model.dataclass.RatingBody
import com.robosoft.foursquare.model.dataclass.favourites.AddFavouriteBody
import com.robosoft.foursquare.model.dataclass.hotel.HotelBody
import com.robosoft.foursquare.model.dataclass.individualhotel.getParticularPlaceDetailsBody
import com.robosoft.foursquare.model.dataclass.individualhotel.getParticularPlaceDetailsResponse
import com.robosoft.foursquare.model.network.ProjectService
import com.robosoft.foursquare.viewModel.HotelDetailViewModel
import com.squareup.picasso.Picasso


class HotelDetailFragment : Fragment() {

    private lateinit var hotelDetailBinding: FragmentHotelDetailBinding
    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: HotelDetailViewModel
    private val projectApi = ProjectService()

    private lateinit var particularPlace: getParticularPlaceDetailsResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        hotelDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_hotel_detail, container, false)
        val bundle = arguments
        val placeId = bundle?.getString("placeId")
        val placeName = bundle?.getString("placeName")
        val distance = bundle?.getString("distance")
        val rating = bundle?.getString("rating")
        var fav = false

        val placeBundle = Bundle()
        placeBundle.putString("placeId", placeId)
        placeBundle.putString("placeName", placeName)

        var starOne = false
        var starTwo = false
        var startThree = false
        var starFour = false
        var starFive = false

        viewModel = ViewModelProvider(this)[HotelDetailViewModel::class.java]


        val sharedPreferences =
            activity?.applicationContext?.getSharedPreferences(
                "sharedPreference",
                Context.MODE_PRIVATE
            )
        val accessToken = activity?.applicationContext?.let { SharedPreferenceManager(it).getAccessToken() }
            val currentLat = sharedPreferences?.getString("currentLat", "")!!
            val currentLong = sharedPreferences?.getString("currentLong", "")!!
            val login = sharedPreferences?.getString("Login", "")!!
            val favData = HotelBody(currentLat,currentLong)
            projectApi.getFavouritePlaceId(accessToken!!,favData) {
                if (it != null) {
                    Log.d("fav place response", it.toString())
                    for (i in it){
                        if (i.placeName == placeName){
                            hotelDetailBinding.favIbn.setImageResource(R.drawable.favourite_whit)
                        }
                    }
                }
            }

            hotelDetailBinding.backIbn.setOnClickListener {
                activity?.finish()
            }

            hotelDetailBinding.favIbn.setOnClickListener {
                if (login == "Login"){
                    if (!fav) {
                        hotelDetailBinding.favIbn.setImageResource(R.drawable.favourite_whit)
                        favourite(placeId!!)
                        fav = true
                    } else {
                        hotelDetailBinding.favIbn.setImageResource(R.drawable.favourite_icon)
                        favourite(placeId!!)
                        fav = false
                    }
                }
                else {
                    Toast.makeText(activity,"Login to add favourite",Toast.LENGTH_SHORT).show()
                }
            }

            hotelDetailBinding.shareIbn.setOnClickListener {
                Toast.makeText(activity?.applicationContext, "share", Toast.LENGTH_SHORT).show()
            }

            hotelDetailBinding.ratingIbn.setOnClickListener {

                var ratingValue = ""

                val builder = AlertDialog.Builder(requireActivity())
                    .create()

                val view = layoutInflater.inflate(R.layout.rating_alertbox, null)
                val closeButton = view.findViewById<ImageButton>(R.id.close_rating)
                val ratingText = view.findViewById<TextView>(R.id.overall_rating)
                val r1 = view.findViewById<ImageView>(R.id.star_one)
                val r2 = view.findViewById<ImageView>(R.id.star_two)
                val r3 = view.findViewById<ImageView>(R.id.star_three)
                val r4 = view.findViewById<ImageView>(R.id.star_four)
                val r5 = view.findViewById<ImageView>(R.id.star_five)

                val submitRating = view.findViewById<TextView>(R.id.submit_rating)
                builder.setView(view)

                ratingText.text = rating

                closeButton.setOnClickListener {
                    builder.dismiss()
                }


                r1.setOnClickListener {
                    if (!starOne) {
                        r1.setImageResource(R.drawable.rating_icon_selected_2x)
                        ratingValue = "1"
                        starOne = true
                        starTwo = true
                        startThree = true
                        starFour = true
                        starFive = true
                    }
                }


                r2.setOnClickListener {
                    if (!starTwo) {
                        r1.setImageResource(R.drawable.rating_icon_selected_2x)
                        r2.setImageResource(R.drawable.rating_icon_selected_2x)
                        ratingValue = "2"
                        starOne = true
                        starTwo = true
                        startThree = true
                        starFour = true
                        starFive = true
                    }
                }


                r3.setOnClickListener {
                    if (!startThree) {
                        r1.setImageResource(R.drawable.rating_icon_selected_2x)
                        r2.setImageResource(R.drawable.rating_icon_selected_2x)
                        r3.setImageResource(R.drawable.rating_icon_selected_2x)
                        ratingValue = "3"
                        starOne = true
                        starTwo = true
                        startThree = true
                        starFour = true
                        starFive = true
                    }
                }


                r4.setOnClickListener {
                    if (!starFour) {
                        r1.setImageResource(R.drawable.rating_icon_selected_2x)
                        r2.setImageResource(R.drawable.rating_icon_selected_2x)
                        r3.setImageResource(R.drawable.rating_icon_selected_2x)
                        r4.setImageResource(R.drawable.rating_icon_selected_2x)
                        ratingValue = "4"
                        starOne = true
                        starTwo = true
                        startThree = true
                        starFour = true
                        starFive = true
                    }
                }



                r5.setOnClickListener {
                    if (!starFive) {
                        r1.setImageResource(R.drawable.rating_icon_selected_2x)
                        r2.setImageResource(R.drawable.rating_icon_selected_2x)
                        r3.setImageResource(R.drawable.rating_icon_selected_2x)
                        r4.setImageResource(R.drawable.rating_icon_selected_2x)
                        r5.setImageResource(R.drawable.rating_icon_selected_2x)
                        ratingValue = "5"
                        starOne = true
                        starTwo = true
                        startThree = true
                        starFour = true
                        starFive = true
                    }
                }
                submitRating.setOnClickListener {
                    val data = RatingBody(placeId!!,ratingValue)

                    if (login != "Login"){
                        Toast.makeText(activity,"Login to add rating",Toast.LENGTH_SHORT).show()
                    }
                    else if (ratingValue == ""){
                        Toast.makeText(activity,"Add rating",Toast.LENGTH_SHORT).show()
                    }
                    else {
                        addRating()
                        getRating(accessToken,data)
                    }
                }
                builder.setCanceledOnTouchOutside(false)
                builder.window?.setBackgroundDrawableResource(R.color.transparent)
                builder.show()
            }

            hotelDetailBinding.photoIbn.setOnClickListener {
                val photo = PhotoFragment()
                photo.arguments = placeBundle
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.hotel_container, photo)?.addToBackStack(null)?.commit()
            }

            hotelDetailBinding.reviewIbn.setOnClickListener {
                val review = ReviewFragment()
                review.arguments = placeBundle
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.hotel_container, review)?.addToBackStack(null)?.commit()
            }

            val data = getParticularPlaceDetailsBody(currentLat!!,currentLong!!,placeId!!)

            viewModel.getHotelDetailDataObserver().observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    Toast.makeText(
                        activity?.applicationContext,
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d("response", it.toString())

                    particularPlace = it

                    val imageUrl = it[0].placeImages.url
                    hotelDetailBinding.backgroundImg.let { it ->
                        val uri = Uri.parse(imageUrl)
                        Picasso.with(activity).load(uri).into(it)
                    }

                    when (it[0].totalrating / 2) {
                        1 -> {
                            hotelDetailBinding.startOne.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startTwo.setImageResource(R.drawable.rating_icon_unselected_2x)
                            hotelDetailBinding.startThree.setImageResource(R.drawable.rating_icon_unselected_2x)
                            hotelDetailBinding.startFour.setImageResource(R.drawable.rating_icon_unselected_2x)
                            hotelDetailBinding.startFive.setImageResource(R.drawable.rating_icon_unselected_2x)
                        }
                        2 -> {
                            hotelDetailBinding.startOne.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startTwo.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startThree.setImageResource(R.drawable.rating_icon_unselected_2x)
                            hotelDetailBinding.startFour.setImageResource(R.drawable.rating_icon_unselected_2x)
                            hotelDetailBinding.startFive.setImageResource(R.drawable.rating_icon_unselected_2x)

                        }
                        3 -> {
                            hotelDetailBinding.startOne.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startTwo.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startThree.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startFour.setImageResource(R.drawable.rating_icon_unselected_2x)
                            hotelDetailBinding.startFive.setImageResource(R.drawable.rating_icon_unselected_2x)

                        }
                        4 -> {
                            hotelDetailBinding.startOne.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startTwo.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startThree.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startFour.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startFive.setImageResource(R.drawable.rating_icon_unselected_2x)

                        }
                        5 -> {
                            hotelDetailBinding.startOne.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startTwo.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startThree.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startFour.setImageResource(R.drawable.rating_icon_selected_2x)
                            hotelDetailBinding.startFive.setImageResource(R.drawable.rating_icon_selected_2x)
                        }
                        else -> {
                            hotelDetailBinding.startOne.setImageResource(R.drawable.rating_icon_unselected_2x)
                            hotelDetailBinding.startTwo.setImageResource(R.drawable.rating_icon_unselected_2x)
                            hotelDetailBinding.startThree.setImageResource(R.drawable.rating_icon_unselected_2x)
                            hotelDetailBinding.startFour.setImageResource(R.drawable.rating_icon_unselected_2x)
                            hotelDetailBinding.startFive.setImageResource(R.drawable.rating_icon_unselected_2x)
                        }
                    }

                    hotelDetailBinding.hotelName.text = it[0].placeName
                    hotelDetailBinding.hotelDesc.text = it[0].keywords
                    hotelDetailBinding.descTv.text = it[0].overview
                    hotelDetailBinding.hotelAddressTv.text = it[0].address
                    hotelDetailBinding.hotelContactTv.text = it[0].phoneNumber
                    hotelDetailBinding.hotelDistanceTv.text = "Drive : $distance Km"

                    val mapFragment = childFragmentManager
                        .findFragmentById(R.id.hotel_map_view) as SupportMapFragment
                    mapFragment.getMapAsync {
                        mMap = it
                        val placeLatLong = LatLng(
                            particularPlace[0].location.coordinates[1],
                            particularPlace[0].location.coordinates[0]
                        )

                        mMap?.apply {
                            addMarker(
                                MarkerOptions().position(placeLatLong)
                                    .title(particularPlace[0].placeName)
                            )
                            moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLong, 12f))
                        }
                    }
                }

            })
            viewModel.getParticularPlaceDetails(data)


            hotelDetailBinding.addReview.setOnClickListener {
                if (login == "Login"){
                    val addReview = AddReviewFragment()
                    addReview.arguments = placeBundle
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.hotel_container, addReview)?.addToBackStack(null)
                        ?.commit()
                }
                else {
                    Toast.makeText(activity,"Login to add review",Toast.LENGTH_SHORT).show()
                }
            }

        return hotelDetailBinding.root
        }
    fun favourite(placeId: String) {
        val sharedPreferences =
            activity?.applicationContext?.getSharedPreferences(
                "sharedPreference",
                Context.MODE_PRIVATE
            )
        val accessToken =
            activity?.applicationContext?.let { SharedPreferenceManager(it).getAccessToken() }
        val data = AddFavouriteBody(placeId)
        projectApi.addToFavourites(accessToken!!, data) {
            if (it == null) {
                Toast.makeText(
                    activity?.applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(activity?.applicationContext, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addRating() {
        viewModel.addRatingDataObserver().observe(viewLifecycleOwner, Observer {
            if (it == null) {
                Toast.makeText(
                    activity?.applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(activity?.applicationContext, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getRating(accessToken: String, data: RatingBody) {
        viewModel.addRating(accessToken, data)
    }

}