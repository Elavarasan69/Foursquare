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
import com.robosoft.foursquare.databinding.FragmentPopularBinding
import com.robosoft.foursquare.model.dataclass.hotel.HotelBody
import com.robosoft.foursquare.viewModel.PopularViewModel

class PopularFragment : Fragment() {
    private lateinit var popularBinding: FragmentPopularBinding
    private lateinit var viewModel: PopularViewModel
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
        popularBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_popular,container, false)
        val data = HotelBody(currentLatLong.latitude.toString(),currentLatLong.longitude.toString())
        viewModel = ViewModelProvider(this)[PopularViewModel::class.java]
        viewModel.getPopularLiveDataObserver().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d("topPickresponse", it.toString())
                popularBinding.popularRecyclerView.layoutManager =
                    LinearLayoutManager(activity?.applicationContext)
                popularBinding.popularRecyclerView.adapter =
                    ViewModelRecyclerAdapter(activity, it,lifecycleScope)
            } else {
                Toast.makeText(
                    activity?.applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        viewModel.popularPlaces(data)

        return popularBinding.root
    }
}