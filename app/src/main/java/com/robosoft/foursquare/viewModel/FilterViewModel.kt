package com.robosoft.foursquare.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robosoft.foursquare.model.dataclass.filter.FilterBody
import com.robosoft.foursquare.model.dataclass.filter.FilterResponse
import com.robosoft.foursquare.model.network.ProjectApi
import com.robosoft.foursquare.model.network.ServiceBuilderObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FilterViewModel: ViewModel() {
    private val retrofit = ServiceBuilderObject.buildService(ProjectApi::class.java)

    private var  FilterLiveDataList: MutableLiveData<FilterResponse?> = MutableLiveData()

    fun getFilterLiveDataObserver(): MutableLiveData<FilterResponse?> {
        return  FilterLiveDataList
    }

    private var  FavFilterLiveDataList: MutableLiveData<FilterResponse?> = MutableLiveData()

    fun getFavFilterLiveDataObserver(): MutableLiveData<FilterResponse?> {
        return  FavFilterLiveDataList
    }

    fun searchByFilter(data: FilterBody) {
        retrofit.searchByFilter(data).enqueue(object : Callback<FilterResponse> {
            override fun onFailure(call: Call<FilterResponse>, t: Throwable) {
                FilterLiveDataList.postValue(null)
                Log.d("search filter response",t.toString())
            }
            override fun onResponse(
                call: Call<FilterResponse>,
                response: Response<FilterResponse>
            ) {
                FilterLiveDataList.postValue(response.body())
                Log.d("search filter response", response.toString())
            }
        })
    }

    fun filterInFavourites(accessToken: String,data: FilterBody) {
        retrofit.filterInFavourites(accessToken,data).enqueue(object : Callback<FilterResponse> {
            override fun onFailure(call: Call<FilterResponse>, t: Throwable) {
                FavFilterLiveDataList.postValue(null)
                Log.d("search filter response",t.toString())
            }
            override fun onResponse(
                call: Call<FilterResponse>,
                response: Response<FilterResponse>
            ) {
                FavFilterLiveDataList.postValue(response.body())
                Log.d("search filter response", response.toString())
            }
        })
    }
}

