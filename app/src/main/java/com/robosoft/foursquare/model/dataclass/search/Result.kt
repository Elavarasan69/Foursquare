package com.robosoft.foursquare.model.dataclass.search

data class Result(
    val _id: String,
    val address: String,
    val dist: Dist,
    val keywords: String,
    val location: Location,
    val placeImages: PlaceImages,
    val placeName: String,
    val priceRange: String,
    val totalrating: Int
)