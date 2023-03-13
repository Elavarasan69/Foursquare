package com.robosoft.foursquare.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.robosoft.foursquare.R
import com.robosoft.foursquare.SharedPreferenceManager
import com.robosoft.foursquare.model.dataclass.favourites.AddFavouriteBody
import com.robosoft.foursquare.model.dataclass.hotel.HotelResponse
import com.robosoft.foursquare.model.network.ProjectService
import com.robosoft.foursquare.view.activity.IndividualHotelContainerActivity
import com.squareup.picasso.Picasso

class FavouriteAdapter(
    private val activity: FragmentActivity?,
    private val data: HotelResponse,
    lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    private val projectApi = ProjectService()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val hotelImg: ImageView = itemView.findViewById(R.id.hotel_img)
        val hotelName: TextView = itemView.findViewById(R.id.hotel_tv)
        val rating: TextView = itemView.findViewById(R.id.rating_tv)
        val desc: TextView = itemView.findViewById(R.id.hotel_type_tv)
        val price: TextView = itemView.findViewById(R.id.hotel_rate_tv)
        val distance: TextView = itemView.findViewById(R.id.hotel_distance_tv)
        val address: TextView = itemView.findViewById(R.id.address_tv)
        val favourite: ImageButton = itemView.findViewById(R.id.remove_btn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_tab, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hotelData = data[position]

        val imageUrl = hotelData.placeImages.url
        holder.hotelImg.let {
            val uri = Uri.parse(imageUrl)
            Picasso.with(activity).load(uri).into(it)
        }
        holder.hotelName.text = hotelData.placeName
        holder.rating.text = hotelData.totalrating.toString()

        val hotelRating = hotelData.totalrating / 2
        if (hotelRating >= 4) {
            holder.rating.setBackgroundResource(R.drawable.custom_rating_green)
        } else if (hotelRating >= 2) {
            holder.rating.setBackgroundResource(R.drawable.custom_rating_yellow)
        } else {
            holder.rating.setBackgroundResource(R.drawable.custom_rating_red)
        }

        holder.desc.text = hotelData.keywords

        when (hotelData.priceRange.length) {
            1 -> {
                holder.price.text = "₹"
            }
            2 -> {
                holder.price.text = "₹₹"
            }
            3 -> {
                holder.price.text = "₹₹₹"
            }
            else -> {
                holder.price.text = "₹₹₹₹"
            }
        }

        val km = (hotelData.dist.calculated).toString()
            .removeRange(4, (hotelData.dist.calculated).toString().length)

        holder.distance.text = "$km km"

        holder.address.text = hotelData.address

        holder.favourite.setOnClickListener {
            data.removeAt(position)
            notifyItemRemoved(position)
            favourite(hotelData._id)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(activity, IndividualHotelContainerActivity::class.java)
            intent.putExtra("placeId", hotelData._id)
            intent.putExtra("placeName", hotelData.placeName)
            intent.putExtra("distance", km)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
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
        projectApi.cancelFromFavourites(accessToken!!, data) {
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

}