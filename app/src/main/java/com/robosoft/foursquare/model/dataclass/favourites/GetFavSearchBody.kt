package com.robosoft.foursquare.model.dataclass.favourites

data class GetFavSearchBody(
    val latitude: String,
    val longitude: String,
    val text: String
)