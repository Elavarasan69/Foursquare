package com.robosoft.foursquare.model.dataclass.review

data class Review(
    val createdOn: String,
    val review: String,
    val userId: UserId
)