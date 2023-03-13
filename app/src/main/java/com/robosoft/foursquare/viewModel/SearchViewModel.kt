package com.robosoft.foursquare.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robosoft.foursquare.model.dataclass.favourites.GetFavSearchBody
import com.robosoft.foursquare.model.dataclass.hotel.HotelBody
import com.robosoft.foursquare.model.dataclass.hotel.HotelResponse
import com.robosoft.foursquare.model.dataclass.nearbyplace.GetNearByCity
import com.robosoft.foursquare.model.dataclass.search.SearchPlaceBody
import com.robosoft.foursquare.model.dataclass.search.SearchPlaceResponseBody
import com.robosoft.foursquare.model.network.ProjectApi
import com.robosoft.foursquare.model.network.ServiceBuilderObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class SearchViewModel: ViewModel() {
    private val retrofit = ServiceBuilderObject.buildService(ProjectApi::class.java)

    private var FavouriteLiveDataList: MutableLiveData<GetNearByCity?> = MutableLiveData()

    private var SearchPlaceLiveDataList: MutableLiveData<SearchPlaceResponseBody?> = MutableLiveData()

    fun getFavouriteLiveDataObserver(): MutableLiveData<GetNearByCity?> {
        return FavouriteLiveDataList
    }

    fun getSearchPlaceLiveDataObserver(): MutableLiveData<SearchPlaceResponseBody?> {
        return SearchPlaceLiveDataList
    }

    fun getNearByCity(data: HotelBody) {
        retrofit.getNearByCity(data).enqueue(object : Callback<GetNearByCity> {
            override fun onFailure(call: Call<GetNearByCity>, t: Throwable) {
                FavouriteLiveDataList.postValue(null)
                Log.d("fav response",t.toString())
            }
            override fun onResponse(
                call: Call<GetNearByCity>,
                response: Response<GetNearByCity>
            ) {
                FavouriteLiveDataList.postValue(response.body())
                Log.d("getFav response", response.toString())
            }
        })
    }

    fun getSearchPlace(data: SearchPlaceBody) {
        retrofit.getSearchPlace(data).enqueue(object : Callback<SearchPlaceResponseBody> {
            override fun onFailure(call: Call<SearchPlaceResponseBody>, t: Throwable) {
                SearchPlaceLiveDataList.postValue(null)
                Log.d("fav response",t.toString())
            }
            override fun onResponse(
                call: Call<SearchPlaceResponseBody>,
                response: Response<SearchPlaceResponseBody>
            ) {
                SearchPlaceLiveDataList.postValue(response.body())
                Log.d("getFav response", response.toString())
            }
        })
    }


}