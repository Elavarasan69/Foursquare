package com.robosoft.foursquare.model.dataclass.hotel

data class HotelResponseItem(
    val _id: String,
    val address: String,
    val dist: Dist,
    val keywords: String,
    val placeImages: PlaceImages,
    val placeName: String,
    val priceRange: String,
    val totalrating: Int
)