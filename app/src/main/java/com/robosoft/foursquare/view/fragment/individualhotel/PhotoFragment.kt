package com.robosoft.foursquare.view.fragment.individualhotel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.robosoft.foursquare.R
import com.robosoft.foursquare.adapter.PhotoAdapter
import com.robosoft.foursquare.databinding.FragmentPhotoBinding
import com.robosoft.foursquare.model.dataclass.review.GetReviewResponseBody
import com.robosoft.foursquare.viewModel.PhotoViewModel


class PhotoFragment : Fragment() {

    private lateinit var photoBinding: FragmentPhotoBinding
    private lateinit var viewModel: PhotoViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        photoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo, container, false)
        val placeBundle = arguments
        val placeId = placeBundle?.getString("placeId")
        val placeName = placeBundle?.getString("placeName")

        photoBinding.photoBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        photoBinding.hotelName.text = placeName
        val data = GetReviewResponseBody(placeId.toString())

        photoBinding.addPhotoIbn.setOnClickListener {
//            activity?.supportFragmentManager?.beginTransaction()
//                ?.replace(R.id.hotel_container, AddReviewFragment())?.addToBackStack(null)?.commit()
        }

        viewModel = ViewModelProvider(this)[PhotoViewModel::class.java]
        viewModel.getPhotoLiveDataObserver().observe(viewLifecycleOwner, Observer {
            if (it == null) {
                Log.d("place id", placeId.toString())
                Toast.makeText(
                    activity?.applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.d("review response", it.toString())

                val reviewImageList = mutableListOf<String>()
                val profileImageList = mutableListOf<String>()
                val userFullNameList = mutableListOf<String>()
                val createdDate = mutableListOf<String>()


                for (i in it.data.reviews) {
                        reviewImageList.addAll(i.reviewImage.urls)
                    profileImageList.addAll(listOf(i.userId.profileImage.public_id))
                    userFullNameList.addAll(listOf(i.userId.fullName))
                    createdDate.addAll(listOf(i.createdOn))
                }

                Log.d("profile image",profileImageList.toString())
//
//                for(i in it.data.reviews){
//                    profileImageList.addAll(listOf(i.userId.profileImage.public_id))
//                }
//
//                for(i in it.data.reviews){
//                    userFullNameList.addAll(listOf(i.userId.fullName))
//                }
//
//                for(i in it.data.reviews){
//                    createdDate.addAll(listOf(i.createdOn))
//                }


                Log.d("review images", reviewImageList.toString())

                photoBinding.photoRecyclerView.layoutManager =
                    GridLayoutManager(activity?.applicationContext, 3)
                photoBinding.photoRecyclerView.adapter =
                    PhotoAdapter(
                        activity,
                        reviewImageList,
                        profileImageList,
                        userFullNameList,
                        createdDate,
                        it,
                        lifecycleScope
                    )
            }
        })
        viewModel.getImagesByPlaceId(data)


        return photoBinding.root
    }


}