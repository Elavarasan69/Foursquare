package com.robosoft.foursquare.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robosoft.foursquare.model.dataclass.RatingResponse
import com.robosoft.foursquare.model.dataclass.RatingBody
import com.robosoft.foursquare.model.dataclass.individualhotel.getParticularPlaceDetailsBody
import com.robosoft.foursquare.model.dataclass.individualhotel.getParticularPlaceDetailsResponse
import com.robosoft.foursquare.model.network.ProjectApi
import com.robosoft.foursquare.model.network.ServiceBuilderObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotelDetailViewModel: ViewModel() {
    private val retrofit = ServiceBuilderObject.buildService(ProjectApi::class.java)

    private var HotelDetailLiveDataList: MutableLiveData<getParticularPlaceDetailsResponse?> = MutableLiveData()

    private var RatinglLiveDataList: MutableLiveData<RatingResponse?> = MutableLiveData()

    fun getHotelDetailDataObserver(): MutableLiveData<getParticularPlaceDetailsResponse?> {
        return HotelDetailLiveDataList
    }

    fun addRatingDataObserver(): MutableLiveData<RatingResponse?> {
        return RatinglLiveDataList
    }

    fun getParticularPlaceDetails(data: getParticularPlaceDetailsBody) {
        retrofit.getParticularPlaceDetails(data).enqueue(object : Callback<getParticularPlaceDetailsResponse> {
            override fun onFailure(call: Call<getParticularPlaceDetailsResponse>, t: Throwable) {
                HotelDetailLiveDataList.postValue(null)
                Log.d("place fail response",t.toString())
            }
            override fun onResponse(
                call: Call<getParticularPlaceDetailsResponse>,
                response: Response<getParticularPlaceDetailsResponse>
            ) {
                HotelDetailLiveDataList.postValue(response.body())
                Log.d("place  response", response.toString())
            }
        })
    }

    fun addRating(accessToken: String,data: RatingBody) {
        retrofit.addRating(accessToken, data).enqueue(object : Callback<RatingResponse> {
            override fun onFailure(call: Call<RatingResponse>, t: Throwable) {
                RatinglLiveDataList.postValue(null)
                Log.d("rating response",t.toString())
            }
            override fun onResponse(
                call: Call<RatingResponse>,
                response: Response<RatingResponse>
            ) {
                RatinglLiveDataList.postValue(response.body())
                Log.d("rating response", response.toString())
            }
        })
    }
}