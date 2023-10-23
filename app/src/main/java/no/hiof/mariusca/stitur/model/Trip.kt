package no.hiof.mariusca.stitur.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Trip(
    @DocumentId val uid: String = "",
    val coordinates: List<Coordinate> = emptyList(),
    val created: Timestamp? = null,
    val difficulty: String = "",
    val geometryType: String = "",
    val lengthInMeters: Long = 0,
    val routeDescription: String = "",
    val routeName: String = "",
    val updated: Timestamp? = null
)

data class Coordinate(
    val lat: String = "",
    val long: String = ""
)