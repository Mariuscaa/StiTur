package no.hiof.mariusca.stitur.model

import com.google.firebase.firestore.DocumentId
import java.util.Date
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

data class TripHistory(
    @DocumentId val uid: String = "",
    val date: Date = Date(),
    val trip: Trip = Trip(),
    var trackedDistanceKm: Double = 0.0,
    var durationMinutes: Int = 0,
    val pointsEarned: Int = 0
)

fun calculateDistanceKM(coordinates: List<Coordinate>): Double {
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
        distance += radius * c
    }

    return round(distance * 100) / 100
}