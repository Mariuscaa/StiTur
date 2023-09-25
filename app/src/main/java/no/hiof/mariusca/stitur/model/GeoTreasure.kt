package no.hiof.mariusca.stitur.model

data class GeoTreasure (
    val geoTreasureID: String = "",
    val title: String = "",
    val textContent: String = "",
    val pictureUrl: String = "",
    val geoLocation: GeoLocation = GeoLocation()
)