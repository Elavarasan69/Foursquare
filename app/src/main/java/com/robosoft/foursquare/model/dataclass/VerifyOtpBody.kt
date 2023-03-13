package com.robosoft.foursquare.model.dataclass

data class VerifyOtpBody(
    val email: String,
    val otp: String
)