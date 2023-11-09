package no.hiof.mariusca.stitur.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

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

fun calculateDistanceMeters(coordinates: List<Coordinate>): Double {
    var distance = 0.0

    for (i in 0 until coordinates.size - 1) {
        val start = coordinates[i]
        val end = coordinates[i + 1]

        val lat1 = Math.toRadians(start.lat.toDouble())
        val lon1 = Math.toRadians(start.long.toDouble())
        val lat2 = Math.toRadians(end.lat.toDouble())
        val lon2 = Math.toRadians(end.long.toDouble())

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        val radius = 6371 // Earth radius in kilometers
        distance += radius * c * 1000 // Convert to meters
    }

    return distance
}