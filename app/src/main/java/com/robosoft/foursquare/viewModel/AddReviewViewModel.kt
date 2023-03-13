package com.robosoft.foursquare.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robosoft.foursquare.model.dataclass.ResponseMessage
import com.robosoft.foursquare.model.dataclass.addreview.ImageList
import com.robosoft.foursquare.model.dataclass.addreview.ReviewBody
import com.robosoft.foursquare.model.dataclass.addreview.UploadImageResponse
import com.robosoft.foursquare.model.dataclass.feedback.FeedbackBody
import com.robosoft.foursquare.model.dataclass.feedback.FeedbackResponse
import com.robosoft.foursquare.model.network.ProjectApi
import com.robosoft.foursquare.model.network.ServiceBuilderObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewViewModel: ViewModel() {

    private val retrofit = ServiceBuilderObject.buildService(ProjectApi::class.java)

    private var ReviewLiveData: MutableLiveData<UploadImageResponse?> = MutableLiveData()

    private var AddReviewLiveData: MutableLiveData<ResponseMessage?> = MutableLiveData()

    fun getReviewDataObserver(): MutableLiveData<UploadImageResponse?> {
        return ReviewLiveData
    }

    fun getAddReviewDataObserver(): MutableLiveData<ResponseMessage?> {
        return AddReviewLiveData
    }

    fun addImage(accessToken: String, placeId: RequestBody, imageList: MutableList<ImageList>?) {
        retrofit.uploadMultipleImages(accessToken, placeId, imageList).enqueue(object : Callback<UploadImageResponse> {
            override fun onFailure(call: Call<UploadImageResponse>, t: Throwable) {
                ReviewLiveData.postValue(null)
                Log.d("add review fail response",t.toString())
            }
            override fun onResponse(
                call: Call<UploadImageResponse>,
                response: Response<UploadImageResponse>
            ) {
                ReviewLiveData.postValue(response.body())
                Log.d("add review success response", response.toString())
            }
        })
    }

    fun addReviewByText(accessToken: String, data: ReviewBody) {
        retrofit.addReviewByText(accessToken, data).enqueue(object : Callback<ResponseMessage> {
            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                AddReviewLiveData.postValue(null)
                Log.d("add review fail response",t.toString())
            }
            override fun onResponse(
                call: Call<ResponseMessage>,
                response: Response<ResponseMessage>
            ) {
                AddReviewLiveData.postValue(response.body())
                Log.d("add review success response", response.toString())
            }
        })
    }
}