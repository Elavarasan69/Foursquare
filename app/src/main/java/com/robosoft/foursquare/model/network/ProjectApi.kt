package com.robosoft.foursquare.model.network

import com.robosoft.foursquare.model.dataclass.*
import com.robosoft.foursquare.model.dataclass.addreview.ImageList
import com.robosoft.foursquare.model.dataclass.addreview.ReviewBody
import com.robosoft.foursquare.model.dataclass.addreview.UploadImageResponse
import com.robosoft.foursquare.model.dataclass.favourites.AddFavouriteBody
import com.robosoft.foursquare.model.dataclass.favourites.GetFavSearchBody
import com.robosoft.foursquare.model.dataclass.favourites.GetFavouritePlaces
import com.robosoft.foursquare.model.dataclass.feedback.FeedbackBody
import com.robosoft.foursquare.model.dataclass.feedback.FeedbackResponse
import com.robosoft.foursquare.model.dataclass.filter.FilterBody
import com.robosoft.foursquare.model.dataclass.filter.FilterResponse
import com.robosoft.foursquare.model.dataclass.forgetpassword.ForgetPasswordBody
import com.robosoft.foursquare.model.dataclass.hotel.HotelBody
import com.robosoft.foursquare.model.dataclass.hotel.HotelResponse
import com.robosoft.foursquare.model.dataclass.individualhotel.getParticularPlaceDetailsBody
import com.robosoft.foursquare.model.dataclass.individualhotel.getParticularPlaceDetailsResponse
import com.robosoft.foursquare.model.dataclass.nearbyplace.GetNearByCity
import com.robosoft.foursquare.model.dataclass.photoReview.PhotoReviewResponse
import com.robosoft.foursquare.model.dataclass.review.GetReviewResponse
import com.robosoft.foursquare.model.dataclass.review.GetReviewResponseBody
import com.robosoft.foursquare.model.dataclass.search.SearchPlaceBody
import com.robosoft.foursquare.model.dataclass.search.SearchPlaceResponseBody
import com.robosoft.foursquare.model.dataclass.signin.SignInBody
import com.robosoft.foursquare.model.dataclass.signin.SignInResponse
import com.robosoft.foursquare.model.dataclass.signup.SignUpBody
import com.robosoft.foursquare.model.dataclass.signup.SignUpResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProjectApi {

    @Headers("Content-Type: application/json")
    @POST("/signIn")
    fun signIn(@Body data : SignInBody) : Call<SignInResponse>

    @Headers("Content-Type: application/json")
    @POST("/signUp")
    fun signUp(@Body data : SignUpBody) : Call<SignUpResponse>

    @Headers("Content-Type: application/json")
    @POST("/forgotPassword")
    fun forgetPassword(@Body data : ForgetPasswordBody) : Call<ResponseMessage>

    @Headers("Content-Type: application/json")
    @POST("/verify")
    fun verifyOtp(@Body data : VerifyOtpBody) : Call<ResponseMessage>

    @Headers("Content-Type: application/json")
    @POST("/resendOtp")
    fun resendOtp(@Body data : ForgetPasswordBody) : Call<ResponseMessage>

    @Headers("Content-Type: application/json")
    @POST("/forgotPassword/createNewPassword")
    fun changePassword(@Body data : ChangePasswordBody) : Call<ResponseMessage>

    @Headers("Content-Type: application/json")
    @POST("/getNearByPlaces")
    fun getNearByPlaces(@Body data : HotelBody) : Call<HotelResponse>

    @Headers("Content-Type: application/json")
    @POST("/topPicksNearYou")
    fun topPicks(@Body data: HotelBody) : Call<HotelResponse>

    @Headers("Content-Type: application/json")
    @POST("/popularPlaces")
    fun popularPlaces(@Body data: HotelBody) : Call<HotelResponse>

    @Headers("Content-Type: application/json")
    @POST("/lunchPlace")
    fun lunchPlace(@Body data: HotelBody) : Call<HotelResponse>

    @Headers("Content-Type: application/json")
    @POST("/cafePlace")
    fun cafePlace(@Body data: HotelBody) : Call<HotelResponse>

    @Headers("Content-Type: application/json")
    @POST("/getParticularPlaceDetails")
    fun getParticularPlaceDetails(@Body data : getParticularPlaceDetailsBody) : Call<getParticularPlaceDetailsResponse>

    @Headers("Content-Type: application/json")
    @POST("/getFavourite")
    fun getFavourite(@Header("authorization") access_token: String, @Body data: HotelBody) : Call<HotelResponse>

    @Headers("Content-Type: application/json")
    @POST("/giveFeedback")
    fun giveFeedback(@Header("authorization") access_token: String, @Body data : FeedbackBody) : Call<FeedbackResponse>

    @Headers("Content-Type: application/json")
    @GET("/aboutUS")
    fun aboutUS() : Call<ResponseMessage>

    @Headers("Content-Type: application/json")
    @POST("/getOnlyReviewsText")
    fun getOnlyReviewsText(@Body data: GetReviewResponseBody) : Call<GetReviewResponse>

    @Headers("Content-Type: application/json")
    @POST("/getImagesByPlaceId")
    fun getImagesByPlaceId(@Body data: GetReviewResponseBody) : Call<PhotoReviewResponse>

    @Headers("Content-Type: application/json")
    @POST("/searchFromFavourite")
    fun searchFromFavourite(@Header("authorization") access_token: String, @Body data: GetFavSearchBody) : Call<HotelResponse>

    @Headers("Content-Type: application/json")
    @POST("/getNearByCity")
    fun getNearByCity(@Body data: HotelBody) : Call<GetNearByCity>

    @Headers("Content-Type: application/json")
    @POST("/searchPlace")
    fun getSearchPlace(@Body data: SearchPlaceBody) : Call<SearchPlaceResponseBody>

    @Headers("Content-Type: application/json")
    @POST("/getName")
    fun getName(@Header("authorization") access_token: String) : Call<NameResponse>

    @Headers("Content-Type: application/json")
    @POST("/searchByFilter")
    fun searchByFilter(@Body data: FilterBody) : Call<FilterResponse>

    @Headers("Content-Type: application/json")
    @PUT("/addToFavourites")
    fun addToFavourites(@Header("authorization") access_token: String, @Body data: AddFavouriteBody) : Call<ResponseMessage>

    @Headers("Content-Type: application/json")
    @PUT("/cancelFromFavourites")
    fun cancelFromFavourites(@Header("authorization") access_token: String, @Body data: AddFavouriteBody) : Call<ResponseMessage>

    @Headers("Content-Type: application/json")
    @PUT("/addRating")
    fun addRating(@Header("authorization") access_token: String, @Body data: RatingBody) : Call<RatingResponse>

    @Headers("Content-Type: application/json")
    @POST("/logout")
    fun logout(@Header("authorization") access_token: String) : Call<LogoutResponse>

    @Headers("Content-Type: application/json")
    @POST("/addReviewByText")
    fun addReviewByText(@Header("authorization") access_token: String, @Body data: ReviewBody) : Call<ResponseMessage>

    @Multipart
    @POST("/addReviews")
    fun uploadMultipleImages(
        @Header("authorization") access_token: String,
        @Part("placeId") placeId: RequestBody,
        @Part("image") imageList: MutableList<ImageList>?) : Call<UploadImageResponse>

    @Headers("Content-Type: application/json")
    @POST("/getFavouritePlaceId")
    fun getFavouritePlaceId(@Header("authorization") access_token: String, @Body data: HotelBody) : Call<GetFavouritePlaces>

    @Headers("Content-Type: application/json")
    @POST("/FilterInFavourites")
    fun filterInFavourites(@Header("authorization") access_token: String, @Body data: FilterBody) : Call<FilterResponse>

}


