package com.robosoft.foursquare.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robosoft.foursquare.model.dataclass.hotel.HotelBody
import com.robosoft.foursquare.model.dataclass.hotel.HotelResponse
import com.robosoft.foursquare.model.network.ProjectApi
import com.robosoft.foursquare.model.network.ServiceBuilderObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NearYouViewModel: ViewModel() {
    private val retrofit = ServiceBuilderObject.buildService(ProjectApi::class.java)

    private var NearYouLiveDataList: MutableLiveData<HotelResponse?> = MutableLiveData()

    fun getNearYouLiveDataObserver(): MutableLiveData<HotelResponse?> {
        return NearYouLiveDataList
    }

    fun getNearByPlaces(data: HotelBody) {
        retrofit.getNearByPlaces(data).enqueue(object : Callback<HotelResponse> {
            override fun onFailure(call: Call<HotelResponse>, t: Throwable) {
                NearYouLiveDataList.postValue(null)
                Log.d("topPick response",t.toString())
            }
            override fun onResponse(
                call: Call<HotelResponse>,
                response: Response<HotelResponse>
            ) {
                NearYouLiveDataList.postValue(response.body())
                Log.d("topPick response", response.toString())
            }
        })
    }
}