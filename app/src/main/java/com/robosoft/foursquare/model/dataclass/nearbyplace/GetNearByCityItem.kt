package com.robosoft.foursquare.model.dataclass.nearbyplace

data class GetNearByCityItem(
    val _id: String,
    val cityName: String,
    val location: Location,
    val placeImage: String
)