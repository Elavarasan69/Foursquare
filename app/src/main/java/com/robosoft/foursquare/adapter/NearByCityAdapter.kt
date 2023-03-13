package com.robosoft.foursquare.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.robosoft.foursquare.R
import com.robosoft.foursquare.model.dataclass.nearbyplace.GetNearByCity
import com.squareup.picasso.Picasso

class NearByCityAdapter(
    private val activity: FragmentActivity?,
    private val data: GetNearByCity,
    lifecycleScope: LifecycleCoroutineScope,
    private val search: SearchView
) : RecyclerView.Adapter<NearByCityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.place_tab, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val city = data[position]

        val imageUrl = city.placeImage
        holder.placeImage.let {
            val uri = Uri.parse(imageUrl)
            Picasso.with(activity).load(uri).into(it)
        }

        holder.placeName.text = city.cityName

        holder.itemView.setOnClickListener {
            search.setQuery(city.cityName, false)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeImage: ImageView = itemView.findViewById(R.id.place_img)
        val placeName: TextView = itemView.findViewById(R.id.place_name)

    }
}