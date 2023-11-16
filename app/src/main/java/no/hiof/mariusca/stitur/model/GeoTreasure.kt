package no.hiof.mariusca.stitur.model

import com.google.firebase.firestore.DocumentId

data class GeoTreasure(
    @DocumentId val geoTreasureID: String = "",
    val title: String = "",
    val textContent: String = "",
    val pictureUrl: String = "",
    val geoLocation: Any = GeoLocation()
)

data class GeoLocation (
    val geoLocationID: String = "",
    val longitude: String = "",
    val latitude: String = ""
)

