package com.robosoft.foursquare.model.dataclass.signup

data class SignUpBody(
    val email: String,
    val fullName: String,
    val mobileNumber: String,
    val password: String
)