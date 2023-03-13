package com.robosoft.foursquare.view.fragment.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.robosoft.foursquare.R
import com.robosoft.foursquare.adapter.ViewModelRecyclerAdapter
import com.robosoft.foursquare.databinding.FragmentLunchBinding
import com.robosoft.foursquare.model.dataclass.hotel.HotelBody
import com.robosoft.foursquare.viewModel.LunchViewModel


class LunchFragment() : Fragment() {
    private lateinit var lunchBinding: FragmentLunchBinding
    private lateinit var viewModel: LunchViewModel
    private lateinit var currentLatLong: LatLng

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val latitude = arguments?.getString("lat")
        val longitude = arguments?.getString("long")
        if (latitude != null) {
            if (longitude != null) {
                currentLatLong = LatLng(latitude.toDouble(), longitude.toDouble())
            }
        }
        Log.d("currentLatLong response", currentLatLong.toString())
        // Inflate the layout for this fragment
        lunchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lunch,container, false)
        val data = HotelBody(currentLatLong.latitude.toString(),currentLatLong.longitude.toString())
        viewModel = ViewModelProvider(this)[LunchViewModel::class.java]
        viewModel.getLunchLiveDataObserver().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d("topPickresponse", it.toString())
                lunchBinding.lunchRecyclerView.layoutManager =
                    LinearLayoutManager(activity?.applicationContext)
                lunchBinding.lunchRecyclerView.adapter =
                    ViewModelRecyclerAdapter(activity, it,lifecycleScope)
            } else {
                Toast.makeText(
                    activity?.applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        viewModel.lunchPlace(data)

        return lunchBinding.root
    }
}