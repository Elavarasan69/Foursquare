package com.robosoft.foursquare.model.dataclass.filter

data class FilterResponseItem(
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