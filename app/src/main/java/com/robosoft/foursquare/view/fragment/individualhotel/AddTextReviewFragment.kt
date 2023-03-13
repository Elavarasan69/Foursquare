package com.robosoft.foursquare.view.fragment.individualhotel

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.robosoft.foursquare.R
import com.robosoft.foursquare.SharedPreferenceManager
import com.robosoft.foursquare.databinding.FragmentAddTextReviewBinding
import com.robosoft.foursquare.model.dataclass.addreview.ReviewBody
import com.robosoft.foursquare.viewModel.AddReviewViewModel


class AddTextReviewFragment : Fragment() {

    private lateinit var addRTextReviewBinding: FragmentAddTextReviewBinding
    private lateinit var viewModel: AddReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        addRTextReviewBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_text_review, container, false)

        val bundle = arguments
        val placeId = bundle?.getString("placeId")
        val placeName = bundle?.getString("placeName")

        addRTextReviewBinding.hotelName.text = placeName

        addRTextReviewBinding.reviewBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel = ViewModelProvider(this)[AddReviewViewModel::class.java]
        addRTextReviewBinding.reviewTextSubmit.setOnClickListener {

            val sharedPreferences =
                activity?.applicationContext?.getSharedPreferences(
                    "sharedPreference",
                    Context.MODE_PRIVATE
                )
            val accessToken =
                SharedPreferenceManager(activity?.applicationContext!!).getAccessToken()
            val review = addRTextReviewBinding.reviewTextEt.text.toString()
            addReviewPage()
            val data = ReviewBody(placeId.toString(),review)
            addReviewBody(accessToken,data)
        }
        return  addRTextReviewBinding.root
    }

    fun addReviewPage() {
        viewModel.getAddReviewDataObserver().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(
                    activity?.applicationContext,
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    activity?.applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun addReviewBody(
        accessToken: String,
        data: ReviewBody
    ) {
        viewModel.addReviewByText(accessToken, data)
    }

}