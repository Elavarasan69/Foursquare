package com.robosoft.foursquare.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robosoft.foursquare.model.dataclass.feedback.FeedbackBody
import com.robosoft.foursquare.model.dataclass.feedback.FeedbackResponse
import com.robosoft.foursquare.model.network.ProjectApi
import com.robosoft.foursquare.model.network.ServiceBuilderObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackViewModel: ViewModel() {

    private val retrofit = ServiceBuilderObject.buildService(ProjectApi::class.java)

    private var FeedbackLiveData: MutableLiveData<FeedbackResponse?> = MutableLiveData()

    fun getFeedbackDataObserver(): MutableLiveData<FeedbackResponse?> {
        return FeedbackLiveData
    }

    fun giveFeedback(accessToken: String, data: FeedbackBody) {
        retrofit.giveFeedback(accessToken, data).enqueue(object : Callback<FeedbackResponse> {
            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                FeedbackLiveData.postValue(null)
                Log.d("feedback response",t.toString())
            }
            override fun onResponse(
                call: Call<FeedbackResponse>,
                response: Response<FeedbackResponse>
            ) {
                FeedbackLiveData.postValue(response.body())
                Log.d("feedback response", response.toString())
            }
        })
    }

}