package com.robosoft.foursquare.adapter

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.gms.maps.model.LatLng
import com.robosoft.foursquare.view.fragment.home.*

class ViewPagerAdapter(fragmentActivity: FragmentActivity, private val currentLatLong: LatLng) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 5
    override fun createFragment(position: Int): Fragment {
        val bundle =  Bundle()
        bundle.putString("lat",currentLatLong.latitude.toString())
        bundle.putString("long",currentLatLong.longitude.toString())

        return when (position){
            0 -> { val nearYou = NearYouFragment()
                nearYou.arguments = bundle
                nearYou
            }
            1 -> {
                val topPick = TopPickFragment()
                topPick.arguments = bundle
                topPick
            }
            2 -> {
                val popular = PopularFragment()
                popular.arguments = bundle
                popular
            }
            3 -> {
                val lunch = LunchFragment()
                lunch.arguments = bundle
                lunch
            }
            4 -> {
                val coffee = CoffeeFragment()
                coffee.arguments = bundle
                coffee
            }
            else -> throw Resources.NotFoundException("Position Not Found")
        }
    }
}