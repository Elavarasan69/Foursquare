package com.robosoft.foursquare.model.dataclass.photoReview

data class Review(
    val createdOn: String,
    val reviewImage: ReviewImage,
    val userId: UserId
)