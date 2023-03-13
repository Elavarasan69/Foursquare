package com.robosoft.foursquare.view.fragment.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.robosoft.foursquare.R
import com.robosoft.foursquare.adapter.ViewModelRecyclerAdapter
import com.robosoft.foursquare.databinding.FragmentNearYouBinding
import com.robosoft.foursquare.model.dataclass.LatLong
import com.robosoft.foursquare.model.dataclass.hotel.HotelBody
import com.robosoft.foursquare.viewModel.NearYouViewModel


class NearYouFragment() : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var nearYouBinding: FragmentNearYouBinding
    private lateinit var viewModel: NearYouViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var currentLatLong: LatLng
//    private lateinit var lastLocation: Location
//    private lateinit var fusedLocationClient: FusedLocationProviderClient

//    companion object {
//        private const val LOCATION_REQUEST_CODE = 1
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val latitude = arguments?.getString("lat")
        val longitude = arguments?.getString("long")
        if (latitude != null) {
            if (longitude != null) {
                currentLatLong = LatLng(latitude.toDouble(), longitude.toDouble())
            }
        }
        Log.d("currentLatLong response", currentLatLong.toString())
        // Inflate the layout for this fragment
        nearYouBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_near_you,container, false)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val data = HotelBody(currentLatLong.latitude.toString(),currentLatLong.longitude.toString())
        viewModel = ViewModelProvider(this)[NearYouViewModel::class.java]
        viewModel.getNearYouLiveDataObserver().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d("topPickresponse", it.toString())
                nearYouBinding.nearYouRecyclerView.layoutManager =
                    LinearLayoutManager(activity?.applicationContext)
                nearYouBinding.nearYouRecyclerView.adapter =
                    ViewModelRecyclerAdapter(activity, it,lifecycleScope)
            } else {
                Toast.makeText(
                    activity?.applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        viewModel.getNearByPlaces(data)


//        fusedLocationClient =
//            LocationServices.getFusedLocationProviderClient(activity?.applicationContext!!)

        return nearYouBinding.root
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap

            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.setOnMarkerClickListener(this)
            setUpMap()
        }
    }

    private fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))

//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            if (location != null) {
//                lastLocation = location
////                val currentLatLong = LatLng(location.latitude, location.longitude)
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
//            }
//        }
    }

    override fun onMarkerClick(p0: Marker?) = true
}