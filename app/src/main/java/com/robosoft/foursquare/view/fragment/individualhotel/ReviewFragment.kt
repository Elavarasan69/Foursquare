package com.robosoft.foursquare.view.fragment.individualhotel

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
import com.robosoft.foursquare.R
import com.robosoft.foursquare.adapter.ReviewAdapter
import com.robosoft.foursquare.databinding.FragmentReviewBinding
import com.robosoft.foursquare.model.dataclass.review.GetReviewResponseBody
import com.robosoft.foursquare.viewModel.ReviewViewModel


class ReviewFragment : Fragment() {

    private lateinit var reviewBinding: FragmentReviewBinding
    private lateinit var viewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        reviewBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_review,container,false)
        val placeBundle = arguments
        val placeId = placeBundle?.getString("placeId")
        val placeName = placeBundle?.getString("placeName")

        reviewBinding.reviewBack.setOnClickListener {
            requireActivity().onBackPressed()
        }


        reviewBinding.addReviewIbn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("placeId", placeId)
            bundle.putString("placeName", placeName)
            val textReview = AddTextReviewFragment()
            textReview.arguments = placeBundle
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.hotel_container, textReview)?.addToBackStack(null)?.commit()
        }

        reviewBinding.hotelName.text = placeName
        val data = GetReviewResponseBody(placeId.toString())


        viewModel = ViewModelProvider(this)[ReviewViewModel::class.java]
        viewModel.getReviewLiveDataObserver().observe(viewLifecycleOwner, Observer {
            if (it?.message == "Review found") {
                Log.d("review response", it.toString())
                reviewBinding.reviewRecyclerView.layoutManager =
                    LinearLayoutManager(activity?.applicationContext)
                reviewBinding.reviewRecyclerView.adapter =
                    ReviewAdapter(activity,it,lifecycleScope)
            } else {
                Log.d("place id", placeId.toString())
                Toast.makeText(
                    activity?.applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        viewModel.getOnlyReviewsText(data)

        return reviewBinding.root
    }
}