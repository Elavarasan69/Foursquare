package com.robosoft.foursquare.adapter

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.robosoft.foursquare.R
import com.robosoft.foursquare.model.dataclass.photoReview.PhotoReviewResponse
import com.robosoft.foursquare.view.fragment.home.NearYouFragment
import com.robosoft.foursquare.view.fragment.individualhotel.IndividualPhotoFragment
import com.squareup.picasso.Picasso

class PhotoAdapter(
    private val activity: FragmentActivity?,
    private val reviewImageList: MutableList<String>,
    private val profileImageList: MutableList<String>,
    private val userFullNameList: MutableList<String>,
    private val createdDate: MutableList<String>,
    private val data:PhotoReviewResponse,
    lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImg: ImageView = itemView.findViewById(R.id.photo_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_review_tab, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val imageUrl = reviewImageList[position]
        holder.profileImg.let {
            val uri = Uri.parse(imageUrl)
            Picasso.with(activity).load(uri).into(it)
        }

        holder.itemView.setOnClickListener {
            val image = reviewImageList[position]
            val profileImg = profileImageList[position]
            val userName = userFullNameList[position]
            val date = createdDate[position]

            val bundle = Bundle()
            bundle.putString("image",image)
            bundle.putString("profileImg", profileImg)
            bundle.putString("userName",userName)
            bundle.putString("date",date)
            val individualPhotoFragment = IndividualPhotoFragment()
            individualPhotoFragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.hotel_container, individualPhotoFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

    }

    override fun getItemCount(): Int {
        return reviewImageList.size
    }
}