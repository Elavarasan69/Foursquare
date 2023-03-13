package com.robosoft.foursquare.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robosoft.foursquare.model.dataclass.photoReview.PhotoReviewResponse
import com.robosoft.foursquare.model.dataclass.review.GetReviewResponseBody
import com.robosoft.foursquare.model.network.ProjectApi
import com.robosoft.foursquare.model.network.ServiceBuilderObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoViewModel: ViewModel() {

    private val retrofit = ServiceBuilderObject.buildService(ProjectApi::class.java)

    private var  PhotoLiveDataList: MutableLiveData<PhotoReviewResponse?> = MutableLiveData()

    fun getPhotoLiveDataObserver(): MutableLiveData<PhotoReviewResponse?> {
        return  PhotoLiveDataList
    }

    fun getImagesByPlaceId(data: GetReviewResponseBody) {
        retrofit.getImagesByPlaceId(data).enqueue(object : Callback<PhotoReviewResponse> {
            override fun onFailure(call: Call<PhotoReviewResponse>, t: Throwable) {
                PhotoLiveDataList.postValue(null)
                Log.d("Review response",t.toString())
            }
            override fun onResponse(
                call: Call<PhotoReviewResponse>,
                response: Response<PhotoReviewResponse>
            ) {
                PhotoLiveDataList.postValue(response.body())
                Log.d("Review response", response.toString())
            }
        })
    }

}