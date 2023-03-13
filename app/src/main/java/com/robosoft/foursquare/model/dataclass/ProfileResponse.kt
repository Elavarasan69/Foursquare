package com.robosoft.foursquare.model.dataclass

data class ProfileResponse(
    val _id: String,
    val fullName: String,
    val mobileNumber: String,
    val profileImage: ProfileImage
)