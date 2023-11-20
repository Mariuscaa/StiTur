package no.hiof.mariusca.stitur.model

import com.google.firebase.firestore.DocumentId

data class GeoTreasure(
    @DocumentId val uid: String = "",
    val title: String = "",
    val textContent: String = "",
    val pictureUrl: String = "",
    val geoLocation: GeoLocation = GeoLocation(),
    val madeBy: MinimalProfile = MinimalProfile()
)

data class GeoLocation(
    val longitude: String = "",
    val latitude: String = ""
)

