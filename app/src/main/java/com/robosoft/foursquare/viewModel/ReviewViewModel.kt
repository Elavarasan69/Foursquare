package com.robosoft.foursquare.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robosoft.foursquare.model.dataclass.review.GetReviewResponse
import com.robosoft.foursquare.model.dataclass.review.GetReviewResponseBody
import com.robosoft.foursquare.model.network.ProjectApi
import com.robosoft.foursquare.model.network.ServiceBuilderObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewViewModel: ViewModel() {

    private val retrofit = ServiceBuilderObject.buildService(ProjectApi::class.java)

    private var  ReviewLiveDataList: MutableLiveData<GetReviewResponse?> = MutableLiveData()

    fun getReviewLiveDataObserver(): MutableLiveData<GetReviewResponse?> {
        return  ReviewLiveDataList
    }

    fun getOnlyReviewsText(data: GetReviewResponseBody) {
        retrofit.getOnlyReviewsText(data).enqueue(object : Callback<GetReviewResponse> {
            override fun onFailure(call: Call<GetReviewResponse>, t: Throwable) {
                ReviewLiveDataList.postValue(null)
                Log.d("Review response",t.toString())
            }
            override fun onResponse(
                call: Call<GetReviewResponse>,
                response: Response<GetReviewResponse>
            ) {
                ReviewLiveDataList.postValue(response.body())
                Log.d("Review response", response.toString())
            }
        })
    }

}