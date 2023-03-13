package com.robosoft.foursquare.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.robosoft.foursquare.R
import com.robosoft.foursquare.adapter.ReviewAdapter.*
import com.robosoft.foursquare.model.dataclass.review.GetReviewResponse
import com.squareup.picasso.Picasso

class ReviewAdapter(
    private val activity: FragmentActivity?,
    private val data: GetReviewResponse,
    lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImg: ImageView = itemView.findViewById(R.id.profile_iv)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val desc: TextView = itemView.findViewById(R.id.review)
        val date: TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_tab, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reviewData = data.data.reviews[position]

        val imageUrl = reviewData?.userId?.profileImage?.public_id
        holder.profileImg.let {
            val uri = Uri.parse(imageUrl)
            Picasso.with(activity).load(uri).into(it)
        }
        holder.userName.text = reviewData.userId.fullName
        holder.date.text = reviewData.createdOn.removeRange(10,(reviewData.createdOn).length)
        holder.desc.text = reviewData.review
    }

    override fun getItemCount(): Int {
        return data.data.reviews.size
    }
}