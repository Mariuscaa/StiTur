package no.hiof.mariusca.stitur.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class TripHistory(
    @DocumentId val uid: String = "",
    val date: Date = Date(),
    val trip: Trip = Trip(),
    val trackedDistanceKm: Double = 0.0,
    val durationMinutes: Int = 0,
    val pointsEarned: Int = 0
)