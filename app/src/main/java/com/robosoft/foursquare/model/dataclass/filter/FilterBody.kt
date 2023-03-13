package com.robosoft.foursquare.model.dataclass.filter

data class FilterBody(
    val acceptCreditCards: Boolean,
    val delivery: Boolean,
    val dogFriendly: Boolean,
    val familyFriendlyPlace: Boolean,
    val inWalkingDistance: Boolean,
    val latitude: String,
    val longitude: String,
    val outdoorSeating: Boolean,
    val parking: Boolean,
    val priceRange: Int,
    val radius: Int,
    val sortBy: String,
    val text: String,
    val wifi: Boolean
)