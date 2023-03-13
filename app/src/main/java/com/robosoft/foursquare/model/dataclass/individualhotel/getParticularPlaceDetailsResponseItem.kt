package com.robosoft.foursquare.model.dataclass.individualhotel

data class getParticularPlaceDetailsResponseItem(
    val _id: String,
    val address: String,
    val dist: Dist,
    val keywords: String,
    val location: Location,
    val overview: String,
    val phoneNumber: String,
    val placeImages: PlaceImages,
    val placeName: String,
    val priceRange: String,
    val totalrating: Int
)