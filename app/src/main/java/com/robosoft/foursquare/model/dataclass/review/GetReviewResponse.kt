package com.robosoft.foursquare.model.dataclass.review

data class GetReviewResponse(
    val `data`: Data,
    val message: String,
    val status: Boolean
)