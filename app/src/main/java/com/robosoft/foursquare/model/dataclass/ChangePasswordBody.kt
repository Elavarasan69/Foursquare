package com.robosoft.foursquare.model.dataclass

data class ChangePasswordBody(
    val email: String,
    val newPassword: String
)